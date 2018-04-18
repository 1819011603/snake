package snake_game;

import javax.swing.*;

public class snakeMain  extends JFrame {
    public snakeMain() {
        snakeWin win = new snakeWin();
        add(win);
        setTitle("̰贪吃蛇v1.0");
        setSize(435, 390);
        setLocation(200, 200);
        setVisible(true);
    }

    // main方法 -> 父类snakeWin构造方法 -> 子类snakeMain构造方法 -> 鼠标点击开始 -> 调用actionPerformed()的if(e.getSource()==newGame)方法块 -> 启动线程 -> run  -> 是否有键盘输入 -> 根据键盘输入调用keyPressed()的哪个方法 -> 没有键盘输入 一直调用 run方法的while(start)方法块 -> 如果有键盘输入 先调用keyPressed()方法吧 (优先性: keyPressed()方法 > run 方法 (加速 变向的本质))

    public static void main(String[] args) {
        new snakeMain();
    }
}
