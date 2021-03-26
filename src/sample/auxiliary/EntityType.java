package sample.auxiliary;

public enum EntityType {
    Player1(1),
    Player2(2),
    Enemy1(3),
    Enemy2(4),
    Enemy3(5),
    Enemy4(6),
    Enemy5(7),
    Tile(8),
    Steel(9),
    Grass(10),
    Water(11);
    private int type;
    EntityType(int type) {
        this.type = type;
    }
}
