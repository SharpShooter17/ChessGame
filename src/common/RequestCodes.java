package common;

public enum RequestCodes {
	//JOIN TO LOBBY	//Client actions
	GET_LOBBY_LIST(100),
	ENTER_LOBBY(101),
	//IN LOBBY
	GET_ROOM_LIST(200),
	CREATE_NEW_ROOM(201),
	ENTER_TO_ROOM(202),
	REFRESH_ROOM_LIST(203),
	BACK_TO_SERVER(204),
	//IN ROOM
	READY_TO_PLAY(300),
	NOT_READY_TO_PLAY(301),
	GIVE_UP(302),
	GAME_STATE(303),
	MOVE(304),
	BACK_TO_LOBBY(305),
	GET_GAME_ROOM_LIST(306),
	SHORT_CASTLING(307),
	LONG_CASTLING(308),

	//SERVER ACTIONS
	GO_TO_LOBBY(400),	//wybierz lobby
	GO_TO_ROOM(401),	//wybierz rooma
	EXIT_FROM_LOBBY(402),
	EXIT_FROM_GAMEROOM(403), //wyjdz z room do lobby
	GO_TO_GAME_ROOM(404), //wejdz do game rooma..
	GAME_START(405),
	SET_PLAYER_COLOR(406),
	RESULT(407),

	//DATA DELIVERY
	LOBBY_LIST(500),
	ROOM_LIST(501),
	GAME_ROOM_LIST(502),
	CLIENT_ID(503),
	SEND_MOVE(504),

	//CLIENT //Client actions
	CLOSE_CONNECTION(600);

	private final int value;

    private RequestCodes(int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
