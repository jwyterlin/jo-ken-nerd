function updatePlayerName(num, name) {
    if (num === 1) {
        var player1Name = document.getElementById('player1name');
        player1Name.innerHTML = name;
    }
    if (num === 2) {
        var player1Name = document.getElementById('player2name');
        player1Name.innerHTML = name;
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
    if(escolhaop1 != -1 && escolhaop2 != -1) {
        document.getElementById('escolha1').innerHTML = escolha1;
        document.getElementById('escolha2').innerHTML = escolha2;
    }
}

