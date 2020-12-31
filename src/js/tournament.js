var tournament;

/**
 * Loads the document. 
 */
function loadTournament() {
    reset();
    tournament = document.getElementById("tournament-name").value;
    loadTable(tournament);
    loadFixture(tournament);
}

/**
 * Resets the table and the fixture.
 */
function reset() {
    document.getElementById("table").innerHTML = "";
    document.getElementById("fixture").innerHTML = "";
}

/**
 * Loads the table and adds it to the document.
 * @param {String} tournament
 */
function loadTable(tournament) {
    const table = document.createElement("table");
    table.appendChild(createTableHead());
    table.appendChild(createTableBody(tournament));
    document.getElementById("table").appendChild(table);
}

/**
 * Gets table information and updates the table.
 * @param {String} tournament
 */
function updateTable(tournament) {
    const tbody = document.getElementById("table").childNodes[0].childNodes[1];
    const info = getTableInformation(tournament);
    for (let i = 0; i < info.length; i++) {
        updateTableRow(tbody.childNodes[i], info[i]);
    }
}

/**
 * Loads the fixture and adds it to the document.
 * @param {String} tournament
 */
function loadFixture(tournament) {
    // First get the round names and create tables for each
    const roundNames = getRoundNames(tournament);
    // Create a round table for every round
    for (const round of roundNames) {
        document.getElementById("fixture").appendChild(createRound(tournament, round));
    }
}

/**
 * Gets the table information from the server
 * @param {String} tournament
 * @return table information, in the format
 * 
 * info ::= [row1, row2, ..., rowN]
 * row ::= "name, w, .., p"
 * where rowi are ordered from highest point to lowest.
 */
function getTableInformation(tournament) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://0.0.0.0:8080/table?tournament=${tournament}`, false);
    xhr.send();
    return xhr.responseText.split("\n");
}

/**
 * Gets the round names from the server.
 * @param {String} tournament
 * @return array of round names
 */
function getRoundNames(tournament) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://0.0.0.0:8080/rounds?tournament=${tournament}`, false);
    xhr.send();
    return xhr.responseText.split(",");
}

/**
 * Gets the information for matches in a round.
 * @param {String} tournament
 * @param {String} round
 * @return match information in the following format:
 * 
 * matches ::= [match1, ..., matchN]
 * match ::= home "," home_score "," away_score "," away
 * 
 * if a score does not exist, returns the string "null" instead.
 */
function getMatchInformation(tournament, round) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://0.0.0.0:8080/matches?tournament=${tournament}&round=${round}`, 
        false);
    xhr.send();
    return xhr.responseText.split("\n");
}

/**
 * Creates a head for the table.
 * @return head
 */
function createTableHead() {
    let head = document.createElement("thead");
    for (let i = 0; i < 8; i++) {
        head.appendChild(document.createElement("th"));
    }
    head.childNodes[0].innerHTML = "Name";
    head.childNodes[1].innerHTML = "W";
    head.childNodes[2].innerHTML = "D";
    head.childNodes[3].innerHTML = "L";
    head.childNodes[4].innerHTML = "SF";
    head.childNodes[5].innerHTML = "SA";
    head.childNodes[6].innerHTML = "SD";
    head.childNodes[7].innerHTML = "P";
    return head;
}

/**
 * Creates a body for the table.
 * @param {String} tournament 
 * @return body
 */
function createTableBody(tournament) {
    const info = getTableInformation(tournament);
    const body = document.createElement("tbody");
    for (const row of info) {
        body.appendChild(createTableRow(row));
    }
    return body;
}

/**
 * Creates a row for the table.
 * @param {String} info
 * @return row
 */
function createTableRow(info) {
    const row = document.createElement("tr");
    for (let i = 0; i < 8; i++) {
        row.appendChild(document.createElement("td"));
    }
    const values = info.split(",");
    row.childNodes[0].innerHTML = values[0];
    row.childNodes[1].innerHTML = values[1];
    row.childNodes[2].innerHTML = values[2];
    row.childNodes[3].innerHTML = values[3];
    row.childNodes[4].innerHTML = values[4];
    row.childNodes[5].innerHTML = values[5];
    row.childNodes[6].innerHTML = values[6];
    row.childNodes[7].innerHTML = values[7];
    for (let i = 1; i < 8; i++) {
        row.childNodes[i].classList.add("table-numeric-value");
    }
    return row;
}

/**
 * Updates the content of a row with new information.
 * @param {tr} row 
 * @param {String} info 
 */
function updateTableRow(row, info) {
    const values = info.split(",");
    row.childNodes[0].innerHTML = values[0];
    row.childNodes[1].innerHTML = values[1];
    row.childNodes[2].innerHTML = values[2];
    row.childNodes[3].innerHTML = values[3];
    row.childNodes[4].innerHTML = values[4];
    row.childNodes[5].innerHTML = values[5];
    row.childNodes[6].innerHTML = values[6];
    row.childNodes[7].innerHTML = values[7];
}

/**
 * Creates a round for the fixture.
 * @param {String} tournament
 * @param {String} round 
 */
function createRound(tournament, round) {
    const table = document.createElement("table");
    table.appendChild(createRoundHead(round));
    table.appendChild(createRoundBody(tournament, round));
    table.classList.add("round");
    return table
}

/**
 * Creates a head for the round.
 * @param {String} round 
 * @return head
 */
function createRoundHead(round) {
    const head = document.createElement("thead");
    const th = document.createElement("th");
    th.innerHTML = round;
    th.colSpan = 5;
    head.appendChild(th);
    return head;
}

/**
 * Creates a body for the round.
 * @param {String} tournament 
 * @param {String} round 
 * @return body 
 */
function createRoundBody(tournament, round) {
    const info = getMatchInformation(tournament, round);
    const body = document.createElement("tbody");
    for (let i = 1; i <= info.length; i++) {
        body.appendChild(createRoundRow(info[i-1], tournament, round, i));
    }
    return body;
}

/**
 * Creates a row for the round
 * @param {String} info 
 * @param {String} round
 * @param {int} game
 * @return row 
 */
function createRoundRow(info, tournament, round, game) {
    const row = document.createElement("tr");
    for (let i = 0; i < 5; i++) {
        row.appendChild(document.createElement("td"));
    }
    const values = info.split(",");
    // 0 and 3 are team names
    row.childNodes[0].innerHTML = values[0];
    row.childNodes[0].classList.add("team-name");
    row.childNodes[0].classList.add("left-aligned");
    row.childNodes[3].innerHTML = values[3];
    row.childNodes[3].classList.add("team-name");
    row.childNodes[3].classList.add("right-aligned");
    // 1 and 2 are scores
    const homeScore = document.createElement("input");
    homeScore.type = "number";
    homeScore.disabled = true;
    if (values[1] !== "null") {
        homeScore.value = values[1];
    }
    const awayScore = document.createElement("input");
    awayScore.type = "number";
    awayScore.disabled = true;
    if (values[2] !== "null") {
        awayScore.value = values[2];
    }
    row.childNodes[1].appendChild(homeScore);
    row.childNodes[2].appendChild(awayScore);
    // Create edit buttons for last column
    const editButton = document.createElement("button");
    editButton.innerHTML = "Edit";
    editButton.classList.add("score-button");
    editButton.onclick = () => {
        editScore(homeScore, awayScore, tournament, round, game);
    }
    row.childNodes[4].appendChild(editButton);
    return row;
}

/**
 * Edit the score.
 * @param {input} homeScoreField 
 * @param {input} awayScoreField 
 * @param {String} round 
 * @param {int} game 
 */
function editScore(homeScoreField, awayScoreField, tournament, round, game) {
    // If disabled, enable fields
    if (homeScoreField.disabled || awayScoreField.disabled) {
        homeScoreField.disabled = false;
        awayScoreField.disabled = false;
        return;
    }
    // If no score provided or invalid, call remove score
    else if (homeScoreField.value.length === 0 || awayScoreField.value.length === 0 
        || homeScoreField.value < 0 || awayScoreField.value < 0) {
        homeScoreField.value = "";
        awayScoreField.value = "";
        removeScoreIfExists(tournament, round, game);
        homeScoreField.disabled = true;
        awayScoreField.disabled = true;
        updateTable(tournament);
    }
    // Else enter score
    else {
        enterScore(tournament, round, game, homeScoreField.value, awayScoreField.value);
        homeScoreField.disabled = true;
        awayScoreField.disabled = true;
    }

}

/**
 * Send a request to remove a score. Updates the table.
 * @param {String} tournament 
 * @param {String} round 
 * @param {int} game 
 */
function removeScoreIfExists(tournament, round, game) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://0.0.0.0:8080/remove?tournament=${tournament}&round=${round}`
     + `&game=${game}`, false);
    xhr.send();
    updateTable(tournament);
}

/**
 * Send a request to enter a score. Updates the table.
 * @param {String} tournament 
 * @param {String} round 
 * @param {int} game 
 * @param {int} homeScore 
 * @param {int} awayScore 
 */
function enterScore(tournament, round, game, homeScore, awayScore) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://0.0.0.0:8080/enter?tournament=${tournament}&round=${round}`
     + `&game=${game}&home=${homeScore}&away=${awayScore}`, false);
    xhr.send();
    updateTable(tournament);
}