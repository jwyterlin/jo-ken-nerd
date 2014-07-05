var cast = window.cast || {};

// Anonymous namespace
(function() {
    'use strict';

    JoKenPo.PROTOCOL = 'com.littleredgroup.jkpls';

    JoKenPo.PLAYER = {
        O: 'O',
        X: 'X'
    };

    function JoKenPo(opt_div) {
        this.div = opt_div;
        this.mPlayer1 = -1;
        this.mPlayer2 = -1;
        this.mCurrentPlayer;

        this.mChannelHandler =
                new cast.receiver.ChannelHandler(JokenPo.PROTOCOL);
        this.mChannelHandler.addEventListener(
                cast.receiver.Channel.EventType.MESSAGE,
                this.onMessage.bind(this));
        this.mChannelHandler.addEventListener(
                cast.receiver.Channel.EventType.OPEN,
                this.onChannelOpened.bind(this));
        this.mChannelHandler.addEventListener(
                cast.receiver.Channel.EventType.CLOSED,
                this.onChannelClosed.bind(this));
    }

    JoKenPo.prototype = {
        /**
         * Channel opened event; checks number of open channels.
         * @param {event} event the channel open event.
         */
        onChannelOpened: function(event) {
            console.log('onChannelOpened. Total number of channels: ' +
                    this.mChannelHandler.getChannels().length);
        },
        /**
         * Channel closed event; if all devices are disconnected,
         * closes the application.
         * @param {event} event the channel close event.
         */
        onChannelClosed: function(event) {
            console.log('onChannelClosed. Total number of channels: ' +
                    this.mChannelHandler.getChannels().length);

            if (this.mChannelHandler.getChannels().length == 0) {
                window.close();
            }
        },
        /**
         * Message received event; determines event message and command, and
         * choose function to call based on them.
         * @param {event} event the event to be processed.
         */
        onMessage: function(event) {
            var message = event.message;
            var channel = event.target;
            console.log('********onMessage********' + JSON.stringify(message));
            console.log('mPlayer1: ' + this.mPlayer1);
            console.log('mPlayer2: ' + this.mPlayer2);

            if (message.command == 'join') {
                this.onJoin(channel, message);
            } else if (message.command == 'leave') {
                this.onLeave(channel);
            } else if (message.command == 'move') {
                this.onMove(channel, message);
            } else if (message.command == 'board_layout_request') {
                this.onBoardLayoutRequest(channel);
            } else {
                cast.log.error('Invalid message command: ' + message.command);
            }
        },
        /**
         * Player joined event: registers a new player who joined the game, or
         * prevents player from joining if invalid.
         * @param {cast.receiver.channel} channel the channel the message came from.
         * @param {Object|string} message the name of the player who just joined.
         */
        onJoin: function(channel, message) {
            console.log('****onJoin: ' + JSON.stringify(message));

            if ((this.mPlayer1 != -1) &&
                    (this.mPlayer1.channel == channel)) {
                this.sendError(channel, 'You are already ' +
                        this.mPlayer1.player +
                        ' You aren\'t allowed to play against yourself.');
                return;
            }
            if ((this.mPlayer2 != -1) &&
                    (this.mPlayer2.channel == channel)) {
                this.sendError(channel, 'You are already ' +
                        this.mPlayer2.player +
                        ' You aren\'t allowed to play against yourself.');
                return;
            }

            if (this.mPlayer1 == -1) {
                this.mPlayer1 = new Object();
                this.mPlayer1.name = message.name;
                this.mPlayer1.channel = channel;
            } else if (this.mPlayer2 == -1) {
                this.mPlayer2 = new Object();
                this.mPlayer2.name = message.name;
                this.mPlayer2.channel = channel;
            } else {
                console.log('Unable to join a full game.');
                this.sendError(channel, 'Game is full.');
                return;
            }

            console.log('mPlayer1: ' + this.mPlayer1);
            console.log('mPlayer2: ' + this.mPlayer2);

            if (this.mPlayer1 != -1 && this.mPlayer2 != -1) {
                //this.startGame_();
                this.div.innerHTML = div.innerHTML + this.mPlayer1 + " vs " + this.mPlayer2;
            }
        },
        /**
         * Player leave event: determines which player left and unregisters that
         * player, and ends the game if all players are absent.
         * @param {cast.receiver.channel} channel the channel of the leaving player.
         */
        onLeave: function(channel) {
            console.log('****OnLeave');

            if (this.mPlayer1 != -1 && this.mPlayer1.channel == channel) {
                this.mPlayer1 = -1;
            } else if (this.mPlayer2 != -1 && this.mPlayer2.channel == channel) {
                this.mPlayer2 = -1;
            } else {
                console.log('Neither player left the game');
                return;
            }
            console.log('mBoard.GameResult: ' + this.mBoard.getGameResult());
            if (this.mBoard.getGameResult() == -1) {
                this.mBoard.setGameAbandoned();
                this.broadcastEndGame(this.mBoard.getGameResult());
            }
        },
        sendError: function(channel, errorMessage) {
            channel.send({event: 'error',
                message: errorMessage});
        },
        broadcastEndGame: function(endState, winningLocation) {
            console.log('****endGame');
            this.mPlayer1 = -1;
            this.mPlayer2 = -1;
            this.broadcast({event: 'endgame',
                end_state: endState,
                winning_location: winningLocation});
        },
        onMove: function(channel, message) {
            console.log('****onMove: ' + JSON.stringify(message));
        },
        /**
         * @private
         */
        startGame_: function() {
            console.log('****startGame');
            var firstPlayer = Math.floor((Math.random() * 10) % 2);
            this.mPlayer1.player = (firstPlayer === 0) ?
                    JoKenPo.PLAYER.X : JoKenPo.PLAYER.O;
            this.mPlayer2.player = (firstPlayer === 0) ?
                    JoKenPo.PLAYER.O : JoKenPo.PLAYER.X;
            this.mCurrentPlayer = JoKenPo.PLAYER.X;

            this.mPlayer1.channel.send({event: 'joined',
                player: this.mPlayer1.player,
                opponent: this.mPlayer2.name});
            this.mPlayer2.channel.send({event: 'joined',
                player: this.mPlayer2.player,
                opponent: this.mPlayer1.name});
        },
        /**
         * Broadcasts a message to all of this object's known channels.
         * @param {Object|string} message the message to broadcast.
         */
        broadcast: function(message) {
            this.mChannelHandler.getChannels().forEach(
                    function(channel) {
                        channel.send(message);
                    });
        }
    }
    cast.JoKenPo = JoKenPo;
})();
