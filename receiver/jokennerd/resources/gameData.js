function updatePlayerName(num, name) {
    if (num === 1) {
        $('#player1name').html(name);
        $("#load1").fadeOut("slow");
    }
    if (num === 2) {
        $('#player2name').html(name);
        $("#load2").fadeOut("slow");
    }
}

var escolhaop1 = -1;
var escolhaop2 = -1;

function updateChoices(c1, c2) {
    $('#escolha1').html('<img src="imagem/' + c1 + '.png"/>');
    $('#escolha2').html('<img src="imagem/' + c2 + '.png"/>');
}

function getResult(c1, c2) {
    if (c1 == c2) {
        updateChoices(c1, c2);
        return 'draw';
    }

    var quadro = [[-1, 2, 1, 1, 5], [2, -1, 3, 4, 2], [1, 3, -1, 3, 5], [1, 4, 3, -1, 4], [5, 2, 5, 4, -1]];
    var result = quadro[c1-1][c2-1];
    /*
    if (result == c1) {
        updateChoices(c1, c2);
        return c1;
    } else {
        updateChoices(c1, c2);
        return c2;
    }
    */
    updateChoices(c1, c2);
    return result;
}

function setResultMsg(result) {
    if (result == 'draw') {
        $('#result').html('Empate');
    } else {
        $('#result').html(result + ' é o vencedor!!!');
    }
}

function setScore(num, score) {
    if (num == 1) {
        $('#score1').html('Pontuação: ' + score)
    }
    if (num == 2) {
        $('#score2').html('Pontuação: ' + score)
    }
}

function resetGame() {
  $('#result').html('');
  $('#escolha1').html('');
  $('#escolha2').html('');
  $('#aguardando1').html('');
  $('#aguardando2').html('');
}
