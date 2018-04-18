package snake_game;

// 蛇是由很多个点构成的 而snakeAct中的(x,y)代表蛇上的一个点
public class snakeAct {
    private int x;
    private int y;
/*    snakeAct(int x, int y){
        this.x = x;
        this.y = y;
    }
    snakeAct(){
        this(0,0);
    }*/
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    // 下列两个方法也可以通过构造函数来实现其功能
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
