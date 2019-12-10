package battle;

import config.Config;
import creature.Bad;
import creature.Creature;
import creature.Good;
import creature.State;
import team.BadTeam;
import team.GoodTeam;
import team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ground {
    private Cell[][] cells ;
    private static final int W = Config.M;
    private static final int H = Config.N;
    private static Ground instance;
    private List<Bullet> bullets;
    public Ground(){
        cells = new Cell[H][W];
        for(int i = 0;i<H;i++){
            for(int j = 0;j<W;j++){
                cells[i][j] = new Cell();
            }
        }
        instance = this;
        bullets = new ArrayList<Bullet>();
    }
    public void update(Team t){
        List<Creature> creatures = t.getTeamMembers();
        for(Creature creature: creatures){
            int x = creature.getX();
            int y = creature.getY();
            if(x<W&&x>=0&&y<H&&y>=0){
                cells[y][x].setCreature(creature);
            }else{
                System.err.println(creature.getClass().getSimpleName()+
                        " out of Ground "+x+" "+y);
            }
        }
    }
    public void setCreature(Creature c, int x, int y){
        cells[y][x].setCreature(c);
    }
    public Creature getCreature(int x, int y){
         if(y<0||y>=H||x<0||x>=W)return null;
         Creature c = cells[y][x].getCreature();
         if(c!=null)System.out.println("ground "+c.getClass().getSimpleName()+" from cell "+x+" "+y+" from creature "+c.getX()+" "+c.getY());
        return cells[y][x].getCreature();
    }
    public static Ground getInstance(){return instance;}
    public void addBullet(Bullet b){
        bullets.add(b);
    }
    public void removeBullet(Bullet b){
        bullets.remove(b);
    }
    public List<Bullet> getBullets(){return bullets;}
    public boolean inGround(int x, int y){
        if(x>=0&&x<W&&y>=0&&y<H)
            return true;
        else return false;
    }
    public Status whoWin(){
        Status status = Status.RUNNING;
        boolean good = true;
        boolean bad = true;
        for(int i = 0;i<H;i++){
            for(int j = 0;j<W;j++){
                Creature c = cells[i][j].getCreature();
                if(c!=null&&c.getState()==State.LIVE){
                    if(c instanceof Good)bad = false;
                    else if(c instanceof Bad)good = false;
                }
            }
        }
        if(good)status = Status.GOODWIN;
        if(bad)status = Status.BADWIN;
        return status;
    }
    public boolean hasEnemy(Creature c){
        int y = c.getY();
        for(int i = 0;i<W;i++){
            Creature t = cells[y][i].getCreature();
            if(t!=null && t.getState()== State.LIVE) {
                if (c instanceof Good && t instanceof Bad)
                    return true;
                if (c instanceof Bad && t instanceof Good)
                    return true;
            }
        }
        return false;
    }
    public int findEnemy(Creature c){
        int y = 0;
        Random rand = new Random();
        int pos = rand.nextInt(100);
        if(pos<50) {
            for (int i = c.getY() - 1; i >= 0; i--) {
                for (int j = 0; j < W; j++) {
                    Creature t = cells[i][j].getCreature();
                    if (t != null && t.getState() == State.LIVE) {
                        if (c instanceof Good && t instanceof Bad) {
                            y = -1;
                            return y;
                        }
                        if (c instanceof Bad && t instanceof Good) {
                            y = -1;
                            return y;
                        }
                    }
                }
            }
        }else if(pos>50) {
            for (int i = c.getY() + 1; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    Creature t = cells[i][j].getCreature();
                    if (t != null && t.getState() == State.LIVE) {
                        if (c instanceof Good && t instanceof Bad) {
                            y = 1;
                            break;
                        }
                        if (c instanceof Bad && t instanceof Good) {
                            y = 1;
                            break;
                        }
                    }
                }
            }
        }
        return y;
    }
    public boolean hasEnemy(Creature c, int y){
        for(int i = 0;i<W;i++){
            Creature t = cells[y][i].getCreature();
            if(t!=null && t.getState()== State.LIVE) {
                if (c instanceof Good && t instanceof Bad)
                    return true;
                if (c instanceof Bad && t instanceof Good)
                    return true;
            }
        }
        return false;
    }
}
