package snake_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// JFrame 是个最外面的窗体，而JPane是个容器，放在JFrame里，然后控件什么的都放在JPane上

/*
ActionListener是swing中的知识，通常是在进行某一个操作的时候触发某项功能。
 举例：当前有个按钮，通过给按钮增加监听事件，之后进行浏览本地路径，添加相应的路径名称，
 之后进行特定的内容操作。此时如果按钮不增加监听，那么就无法实现打开文件浏览功能。
*/

// KeyListener  是java 中的一个接口，用于接收键盘事件（击键）的侦听器接口

// Runnable 是java中的一个使用线程的一个接口
public class snakeWin extends JPanel implements ActionListener,KeyListener,Runnable{
    private int fenShu=0,Speed=1;
    private boolean start = false;
    // 食物坐标
    private int rx=0,ry=0;
    private int eat1=2;

    // JDialog 对话框类
    private JDialog dialog = new JDialog();
    // JLabel 标签类
    private JLabel label = new JLabel("你挂了！你的分数是"+fenShu+"。");
    // JButton 按钮类
    private JButton ok = new JButton("请点击 T_T");

    // Random 随机类 实现随机食物(有可能会出现在蛇身上 有点小bug 懒得改了  判断是否与list中某个点相等 如果有 再生成一个随机食物 再进行判断 直到食物不在蛇身上)
    private Random r = new Random();
    private JButton newGame,stopGame,pauseGame,continueGame;

    // 蛇就是这个list  蛇是一个一个点构成的集合
    private List<snakeAct> list = new ArrayList<>();
    private int temp=0;
    private Thread nThread;

    snakeWin() {
        newGame = new JButton("开始");
        pauseGame = new JButton("暂停");
        continueGame = new JButton("继续");
        stopGame = new JButton("结束");
        newGame.addActionListener(this);
        pauseGame.addActionListener(this);
        continueGame.addActionListener(this);
        stopGame.addActionListener(this);
        this.addKeyListener(this);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(newGame);
        this.add(stopGame);
        this.add(continueGame);
        this.add(pauseGame);
        pauseGame.setEnabled(false);
        continueGame.setEnabled(false);
        // 把这个窗口分成两行一列
        dialog.setLayout(new GridLayout(2, 1));
        dialog.add(label);
        dialog.add(ok);
        dialog.setSize(200, 200);
        dialog.setLocation(200, 200);
        dialog.setVisible(false);
        ok.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==newGame){
            // set the start button as not enable clicked
            // assign the value as Default true
            newGame.setEnabled(false);
            pauseGame.setEnabled(true);
            start = true;
            rx=r.nextInt(40);ry=r.nextInt(30);
            // 将蛇的长度初始化为1
            snakeAct tempAct = new snakeAct();
            tempAct.setX(20);
            tempAct.setY(15);
            list.add(tempAct);
            tempAct = new snakeAct();
            tempAct.setX(19);
            tempAct.setY(15);
            list.add(tempAct);
            // 把光标放在这个窗口内部
            this.requestFocus();
            // 开启本线程( newGame 和 continueGame 中都是同样的方式启动线程的)
            nThread = new Thread(this);
            nThread.start();
            repaint();
        }
        if(e.getSource() == pauseGame) {
            start = false;
            pauseGame.setEnabled(false);
//            System.out.println(Thread.currentThread().getName() + " pauseGame");
            continueGame.setEnabled(true);
        }

        if(e.getSource() == continueGame){
            start = true;
            pauseGame.setEnabled(true);
            continueGame.setEnabled(false);

            this.requestFocus();
            nThread = new Thread(this);
            nThread.start();
            repaint();
        }

        if(e.getSource()==stopGame){
            System.exit(0);
        }

        if(e.getSource()==ok){
            // 清除蛇的长度 将弹出的窗口关闭(设置为不可见)
            // 初始化速度和分数
            list.clear();
            start=false;
            newGame.setEnabled(true);
            dialog.setVisible(false);
            fenShu = 0;
            temp = 0;
            Speed = 1;
            eat1 = 2;
            // 擦去以前的图像 并显示当前图像
            repaint();

        }
    }

    // 调用repaint就会调用该函数
// 画图 动态的运行的本质重复地清除旧的图片 并重新写入新的图片
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawString("分数："+fenShu, 270, 15);
        g.drawString("速度："+Speed, 270, 35);
        g.drawString("长度："+eat1, 350, 15);
        g.setColor(new Color(212,250,242));
        g.fillRect(10,40,400,300);
        g.setColor(new Color(216,213,82));
        for (int i = 10; i <= 410; i+=10){
            g.drawLine(i,40,i,340);
        }
        for (int i = 40; i <= 340; i+=10){
            g.drawLine(10,i,410,i);
        }
        // set the color of the food
        g.setColor(new Color(0, 255, 0));
        if(start){
            g.fillRect(10+rx*10, 40+ry*10, 10, 10);
            for (int i = 0; i < list.size(); i++) {
                // set the color of snake
                g.setColor(new Color(255, 49, 62));
                // paint the snake
                g.fillRect(10+list.get(i).getX()*10, 40+list.get(i).getY()*10, 10, 10);
            }
        }
    }
    // 判断食物是不是在蛇身上
    private boolean isSnake(int rx, int ry)
    {
        for (int i = 0; i < list.size(); i++) {
            if (rx==list.get(i).getX()&&ry==list.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    // 吃到食物之后将蛇的末尾加一
    private void eat(){

        if (rx==list.get(0).getX()&&ry==list.get(0).getY()) {
            // 构造下一个食物的坐标
            rx = r.nextInt(40);
            ry = r.nextInt(30);
            // 判断是否在蛇身上
            while (isSnake(rx,ry)){
                rx = r.nextInt(40);
                ry = r.nextInt(30);
            }
            // 蛇的长度要增加1 而且要在蛇尾后加一 所以要知道蛇末尾的朝向才知道加在哪呗 要不然四不像
            snakeAct tempAct = new snakeAct();
            int x_tail = list.get(list.size()-1).getX();
            int y_tail = list.get(list.size()-1).getY();
            // 只要知道原先倒数一二点和新点在同一条直线上  所以三个点x,y成等差数列 然后加到list末尾
            tempAct.setX(x_tail + (x_tail - list.get(list.size()-2).getX()));
            tempAct.setY(y_tail + (y_tail - list.get(list.size()-2).getY()));
            list.add(tempAct);
            fenShu = fenShu+100*Speed+10;
            eat1++;
            // 每吃四个就提速
            if((eat1-2) % 4 == 0) {
                Speed++;
            }
        }
    }

    // 移动的时候先把末尾的那一节点删去 然后在头部插入一个蛇头
    public void otherMove(int x, int y){
        list.remove(list.size()-1);
        snakeAct tempAct = new snakeAct();
        tempAct.setX(list.get(0).getX()+x);
        tempAct.setY(list.get(0).getY()+y);
        list.add(0,tempAct);
    }

    public void move(int x,int y){
        // 判断蛇下一步会不会死亡
//        System.out.println("  move");
        if (NotMoveCrash(x, y)) {
            //移动
            otherMove(x,y);
            // 移动后是否吃到食物
            eat();
//          重新刷新蛇和食物的图像
            repaint();
        }else {
            // 蛇死亡后
            nThread = null;
            start = false;
            label.setText("你挂了！你的分数是"+fenShu+"。");
            dialog.setVisible(true);
            pauseGame.setEnabled(false);
        }

    }

    // 没有移动冲突
    // 判断即将到来的动作(移动蛇的方向)会不会使蛇死亡
    // false == 死亡
    // true == 存活
    public boolean NotMoveCrash(int x,int y){

        if (!isCrash(list.get(0).getX()+x,list.get(0).getY()+ y)) {
            return false;
        }
        return true;
    }
    /*
     判断是否违规
     false == 死亡
     true == 继续存活
    */
    public boolean isCrash(int x,int y){
        if (x<0||x>=40||y<0||y>=30) {
            return false;
        }
        for (int i = 3; i < list.size(); i++) {
            if (list.get(0).getX()==list.get(i).getX()&&list.get(0).getY()==list.get(i).getY()) {
                return false;
            }
        }
        return true;
    }

    // 读取从键盘来的上下左右信息代码
    public void keyPressed(KeyEvent e) {
        if(start){
            // 蛇加速的关键之处 只要有按键  就会调用move方法  使之不用sleep  就会加速
            // 速度不是非常快的原因就是  按键时的判断需要时间 多少时间以后才会接受下一个按键值
            // 判断是否有键盘动作
            // 判断具体是上下左右哪一个动作 其他动作无效
            // 判断键盘动作是否合理   不合理动作 例如: 蛇在向下走的时候 按向上走是无效的(如果有效的话 蛇肯定会吃到自己 GAMEOVER)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (temp != 2)
                    {
                        move(0, -1);
                        temp=1;
                    }
                    break;
                case KeyEvent.VK_DOWN:

                    if (temp != 1){
                        move(0, 1);
                        temp=2;
                    }
                    break;
                case KeyEvent.VK_LEFT:

                    if (temp != 4){
                        move(-1, 0);
                        temp=3;
                    }

                    break;
                case KeyEvent.VK_RIGHT:

                    if (temp != 3){
                        move(1, 0);
                        temp=4;
                    }
                    break;

                default:
                    break;
            }
        }
//        System.out.print("keyPressed temp " + temp );
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    // 线程
    public void run() {
        // while(true)  程序一直运行
        // false之后  该线程结束
        while (start) {
            // 保持temp方向直行  不能使蛇加速
//            System.out.print("run temp " + temp);
            switch (temp) {
                case 1:
                    move(0, -1);
                    break;
                case 2:
                    move(0, 1);
                    break;
                case 3:
                    move(-1, 0);
                    break;
                case 4:
                    move(1, 0);
                    break;
                default:
                    break;
            }
            // 蛇移动了之后肯定需要重画啊
            repaint();
            // 不休眠的话你可能根本没有游戏体验
            try {
                // 线程休眠的时间减少等价于蛇的速度越来越快了
                // 这才是速度变化的关键处
                Thread.sleep(300-10*Speed);
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }

    }

}
