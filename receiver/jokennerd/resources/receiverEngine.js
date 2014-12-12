var mPlayer1 = -1;
var mPlayer2 = -1;

function execTest(teste) {
  //var player1Name = document.getElementById('player1name');
  //player1Name.innerHTML = teste;
  $('#player1name, #player2name').html(teste);
}

window.onload = function() {
    cast.receiver.logger.setLevelValue(0);
    window.castReceiverManager = cast.receiver.CastReceiverManager.getInstance();
    console.log('Starting Receiver Manager JKPLS');

    // handler for the 'ready' event
    castReceiverManager.onReady = function(event) {
        console.log('Received Ready event: ' + JSON.stringify(event.data));
        window.castReceiverManager.setApplicationState("Application status is ready...");
    };

    // handler for 'senderconnected' event
    castReceiverManager.onSenderConnected = function(event) {
        console.log('Received Sender Connected event: ' + event.data);
        console.log(window.castReceiverManager.getSender(event.data).userAgent);
    };

    // handler for 'senderdisconnected' event
    castReceiverManager.onSenderDisconnected = function(event) {
        console.log('Received Sender Disconnected event: ' + event.data);
        mPlayer1.score = 0;
        mPlayer2.score = 0;
        if (event.senderId == mPlayer1.senderId) {
            mPlayer1 = -1;
            $('#player1name').html('Aguardando jogador...');
            $("#load1").fadeIn("slow");
        }
        if (event.senderId == mPlayer2.senderId) {
            mPlayer2 = -1;
            $('#player2name').html('Aguardando jogador...');
            $("#load2").fadeIn("slow");
        }
        setScore(1, 0);
        setScore(2, 0);
        resetGame();

        if (window.castReceiverManager.getSenders().length == 0) {
            window.close();
        }
    };

    // handler for 'systemvolumechanged' event
    castReceiverManager.onSystemVolumeChanged = function(event) {
        console.log('Received System Volume Changed event: ' + event.data['level'] + ' ' +
                event.data['muted']);
    };

    // create a CastMessageBus to handle messages for a custom namespace
    window.messageBus =
            window.castReceiverManager.getCastMessageBus(
                    'urn:x-cast:com.littleredgroup.jkpls');

    // handler for the CastMessageBus message event
    window.messageBus.onMessage = function(event) {
        var json = JSON.parse(event.data);
        actions(json.action, json, event.senderId);
    }

    // initialize the CastReceiverManager with an application status message
    window.castReceiverManager.start({statusText: "Application is starting"});
    console.log('Receiver Manager started');
};

function actions(action, data, senderId) {
    if (action == 'connect') {
        connect(data.name, senderId);
    }
    if (action == 'update') {
        update(senderId, data.name);
    }
    if (mPlayer1 != -1 && mPlayer2 != -1) {
        if (action == 'choice') {
            choice(data.value, senderId);
        }
    }
}


function update(senderId, name) {
    if (mPlayer1.senderId == senderId) {
        mPlayer1.name = name;
        $('#player1name').html = mPlayer1.name;
    }
    if (mPlayer2.senderId == senderId) {
        mPlayer2.name = name;
        $('#player2name').html = mPlayer2.name;
    }
}

function choice(value, senderId) {
    console.log('choice: ' + value);
    if (mPlayer1.senderId == senderId) {
        console.log('player 1 choice: ' + value);
        mPlayer1.choice = value;
        $('#aguardando2').html('');
        $("#load2").fadeOut("slow");
        if (mPlayer2.choice == -1) {
            $('#aguardando1').html('Aguardando oponente');
            $('#escolha2').html('');
            $('#escolha1').html('');
            $("#load1").fadeIn("slow");
            $('#result').html('');
        }
    }
    if (mPlayer2.senderId == senderId) {
        console.log('player 2 choice: ' + value);
        mPlayer2.choice = value;
        $('#aguardando2').html('');
        $("#load1").fadeOut("slow");
        if (mPlayer1.choice == -1) {
            $('#aguardando2').html('Aguardando oponente');
            $('#escolha1').html('');
            $('#escolha2').html('');
            $("#load2").fadeIn("slow");
            $('#result').html('')
        }
    }

    if (mPlayer1 != -1 && mPlayer2 != -1) { //verifica se ambos jogadores estao conectados
        if (mPlayer1.choice != -1 && mPlayer2.choice != -1) { //verifica se ambos jogadores fizeram jogada
            var result = getResult(mPlayer1.choice, mPlayer2.choice); //recupera o resultado
            console.log('result: ' + result);
            if (result == mPlayer1.choice) {
                setResultMsg(mPlayer1.name);
                window.messageBus.send(mPlayer1.senderId, '{ "result":"win" }');
                window.messageBus.send(mPlayer2.senderId, '{ "result":"loses" }');
                mPlayer1.score++;
                setScore(1, mPlayer1.score);
            }
            if (result == mPlayer2.choice) {
                setResultMsg(mPlayer2.name);
                window.messageBus.send(mPlayer2.senderId, '{ "result":"win" }');
                window.messageBus.send(mPlayer1.senderId, '{ "result":"loses" }');
                mPlayer2.score++;
                setScore(2, mPlayer2.score);
            }
            if (result == 'draw') {
                setResultMsg('draw');
                window.messageBus.broadcast('{ "result":"draw" }');
            }
            mPlayer1.choice = -1;
            mPlayer2.choice = -1;
            $('#aguardando1, #aguardando2').html('');
        }
    }
}

function connect(nome, senderId) {

    if ((mPlayer1 != -1) && (mPlayer1.senderId == senderId)) {
        window.messageBus.send(mPlayer1.senderId, '{ "error":"already_connected" }');
        return;
    }
    if ((mPlayer2 != -1) && (mPlayer2.senderId == senderId)) {
        window.messageBus.send(mPlayer2.senderId, '{ "error":"already_connected" }');
        return;
    }

    if (mPlayer1 == -1) {
        mPlayer1 = new Object();
        mPlayer1.name = nome;
        mPlayer1.senderId = senderId;
        mPlayer1.choice = -1;
        mPlayer1.score = 0;
        updatePlayerName(1, nome);
        console.log('Jogador 1 entrou ' + mPlayer1.name);
    } else if (mPlayer2 == -1) {
        mPlayer2 = new Object();
        mPlayer2.name = nome;
        mPlayer2.senderId = senderId;
        mPlayer2.choice = -1;
        mPlayer2.score = 0;
        updatePlayerName(2, nome);
        console.log('Jogador 2 entrou ' + mPlayer2.name);
    } else {
        console.log('Unable to join a full game.');
        window.messageBus.send(senderId, '{ "error":"room_full"}');
        return;
    }

    if (mPlayer1 != -1 && mPlayer2 != -1) {
        window.messageBus.broadcast('{ "sucess":"sucess_conected" }');
    }
}
;
