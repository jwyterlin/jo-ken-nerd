function updatePlayerName(num, name) {
    if (num === 1) {
        var player1Name = document.getElementById('player1name');
        player1Name.innerHTML = name;
        $("#load1").fadeOut("slow");
    }
    if (num === 2) {
        var player2Name = document.getElementById('player2name');
        player2Name.innerHTML = name;
        $("#load2").fadeOut("slow");
    }
}

var escolhaop1 = -1;
var escolhaop2 = -1;
function move(oponente, escolha) {
    if (oponente === 1) {
        escolhaop1 = escolha;
    }
    if (oponente === 2) {
        escolhaop2 = escolha;
    }
    if (escolhaop1 != -1 && escolhaop2 != -1) {
        document.getElementById('escolha1').innerHTML = escolha1;
        document.getElementById('escolha2').innerHTML = escolha2;
    }
}

function updateChoices(c1, c2) {
    document.getElementById('escolha1').innerHTML = c1;
    document.getElementById('escolha2').innerHTML = c2;
}

function getResult(c1, c2) {
    if (c1 == c2) {
        updateChoices(c1, c2);
        return 'draw';
    }

    var quadro = [[-1, 2, 1, 1, 5], [2, -1, 3, 4, 2], [1, 3, -1, 3, 5], [1, 4, 3, -1, 4], [5, 2, 5, 4, -1]];
    var result = quadro[c1-1][c2-1];
    if (result == c1) {
        updateChoices(c1, c2);
        return c1;
    } else {
        updateChoices(c1, c2);
        return c2;
    }
}

function setResultMsg(result) {
    if (result == 'draw') {
        document.getElementById('result').innerHTML = 'Empate';
    } else {
        document.getElementById('result').innerHTML = result + ' é o vencedor!!!';
    }
}
