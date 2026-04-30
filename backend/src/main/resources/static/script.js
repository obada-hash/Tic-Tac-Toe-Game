// Config
const BASE_URL = "http://localhost:8080";
const WS_URL = `${BASE_URL}/game-websocket`;

// Global state
let gameId = null;
let myToken = null;
let myMark = null;
let stompClient = null;

// UI Helpers
function showScreen(name) {
    document.querySelectorAll(".screen").forEach(s => s.classList.remove("active"));
    document.getElementById(`screen-${name}`).classList.add("active");
}

function showError(msg) {
    const el = document.getElementById("lobby-error");
    el.textContent = msg;
    el.classList.toggle("hidden", !msg);
}

// --- Lobby Logic ---

document.getElementById("btn-create").addEventListener("click", async () => {
    showError("");
    try {
        const res = await fetch(`${BASE_URL}/api/v1/games/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ isPlayerOneTurn: true })
        });

        if (!res.ok) throw new Error("Failed to create game");
        const data = await res.json();

        gameId = data.id;
        myToken = data.player1.token;
        myMark = "X"; // creator is always X

        document.getElementById("display-game-id").textContent = gameId;
        showScreen("waiting");
        initWebSocket();
    } catch (err) {
        showError(err.message);
    }
});

document.getElementById("btn-join").addEventListener("click", async () => {
    const id = document.getElementById("input-game-id").value.trim();
    showError("");

    if (!id) {
        showError("Please enter a Game ID");
        return;
    }

    try {
        const res = await fetch(`${BASE_URL}/api/v1/games/join`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id })
        });

        if (!res.ok) throw new Error("Failed to join game. It might be full or the ID is wrong.");
        const data = await res.json();

        gameId = data.id;
        myToken = data.player2.token;
        myMark = "O";

        showScreen("game");
        updateBoardUI(data);
        initWebSocket();
    } catch (err) {
        showError(err.message);
    }
});

document.getElementById("btn-new-game").addEventListener("click", () => {
    if (stompClient) stompClient.disconnect();
    showScreen("lobby");
});

document.getElementById("btn-copy").addEventListener("click", (e) => {
    navigator.clipboard.writeText(gameId).then(() => {
        e.target.textContent = "Copied!";
        setTimeout(() => e.target.textContent = "Copy ID", 1500);
    });
});

// --- WebSocket Setup ---

function initWebSocket() {
    const socket = new SockJS(WS_URL);
    stompClient = Stomp.over(socket);
    stompClient.debug = null; // disables console spam

    stompClient.connect({}, () => {
        stompClient.subscribe(`/radio/game/${gameId}`, (msg) => {
            const data = JSON.parse(msg.body);

            // if we're still waiting, switch to the game screen
            if (document.getElementById("screen-waiting").classList.contains("active")) {
                showScreen("game");
            }
            updateBoardUI(data);
        });
    }, (err) => {
        console.error("WS Error:", err);
        document.getElementById("game-error").textContent = "Connection lost to server.";
    });
}

// --- Game Logic ---

function updateBoardUI(gameData) {
    const cells = document.querySelectorAll(".cell");

    // Convert "---------" string to array
    const boardArr = gameData.board ? gameData.board.split('') : [];

    boardArr.forEach((val, i) => {
        const cell = cells[i];
        const mark = val === '-' ? "" : val;

        cell.textContent = mark;
        cell.className = "cell"; // reset classes
        if (mark) cell.classList.add("taken", mark.toLowerCase());
    });

    updateGameStatus(gameData);
}

function updateGameStatus(gameData) {
    const statusEl = document.getElementById("status-text");
    const state = gameData.gameState;

    if (!gameData.player2) {
        statusEl.textContent = "Waiting for opponent...";
        return;
    }

    // Handle end game states
    if (["PLAYER_ONE_WINS", "PLAYER_TWO_WINS", "DRAW"].includes(state)) {
        if (state === "DRAW") {
            statusEl.textContent = "It's a draw!";
        } else {
            const winner = state === "PLAYER_ONE_WINS" ? "X" : "O";
            statusEl.textContent = winner === myMark ? "🎉 You win!" : `Player ${winner} wins!`;
        }
        // lock the board
        document.querySelectorAll(".cell").forEach(c => c.classList.add("taken"));
        return;
    }

    // Handle active turns
    const isMyTurn = (gameData.isPlayerOneTurn && myMark === "X") ||
        (!gameData.isPlayerOneTurn && myMark === "O");

    statusEl.textContent = isMyTurn ? "Your turn!" : "Waiting for opponent...";
}

// Board click events
document.querySelectorAll(".cell").forEach(cell => {
    cell.addEventListener("click", (e) => {
        if (e.target.classList.contains("taken")) return;
        if (!stompClient || !stompClient.connected) return;

        const index = Number(e.target.dataset.index);

        // Note: backend expects indices 1-9, not 0-8
        stompClient.send("/app/gameplay", {}, JSON.stringify({
            index: index + 1,
            id: gameId,
            token: myToken
        }));
    });
});