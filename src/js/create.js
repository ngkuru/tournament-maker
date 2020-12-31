const teamList = [];

/**
 * Reset the state of the document.
 */
function reset() {
    document.getElementById("team-name").value = "";
    document.getElementById("tournament-name").value = "";
    document.getElementById("team-list").innerHTML = "";
    teamList.splice(0, teamList.length)
}

/**
 * Add a team if there is a team name present, else alert.
 */
function addButton() {
    const name = document.getElementById("team-name").value;
    if (name.length > 0) {
        addTeam(name);
        document.getElementById("team-name").value = "";
    } else {
        alert("Team name can't be blank!");
    }
}

/**
 * Add a team to the team list.
 * @param {String} team 
 */
function addTeam(team) {
    teamList.push(team);
    document.getElementById("team-list").appendChild(createTeamListElement(team));
}

/**
 * Remove a team from the team list.
 * @param {String} team
 * @param {li} li list element containing the team
 */
function removeTeam(team, li) {
    teamList.splice(teamList.indexOf(team), 1);
    li.parentElement.removeChild(li);
}

/**
 * Create a list element for a team.
 * @param {String} team 
 */
function createTeamListElement(team) {
    const li = document.createElement("li");
    li.appendChild(document.createTextNode(team));
    const removeButton = document.createElement("button");
    removeButton.innerHTML = "Remove";
    removeButton.onclick = () => {
        removeTeam(team, li);
    }
    li.appendChild(removeButton);
    return li
}

/**
 * Create a tournament with the given teams and name.
 */
function create() {
    if (teamList.length < 2) {
        alert("You need at least two teams to create a tournament!");
        return;
    }
    const tournament = document.getElementById("tournament-name").value;
    if (tournament.length === 0) {
        alert("Tournament name can't be blank!");
        return;
    }
    let teams = "";
    for (let i = 0; i < teamList.length; i++) {
        if (i !== 0) {
            teams += ",";
        }
        teams += teamList[i];
    }
    console.log(teams);
    const response = createTournament(tournament, teams);
    if (response === "success") {
        reset();
        alert("Tournament created!");
    } else {
        alert("Failed to create.");
    }
}

/**
 * Send a request to create the tournament.
 * @param {String} tournament 
 * @param {String} teams 
 */
function createTournament(tournament, teams) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://0.0.0.0:8080/create?tournament=${tournament}&teams=${teams}`, false);
    xhr.send();
    return xhr.responseText;
}