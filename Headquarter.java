package WOW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Headquarter {
    static int wowTime = 0;
    static int numCity;
    static int loyaltyDecrease;
    int life;// 生命元
    boolean stop = false;// 是否停止造武士
    String army;// 阵营
    int[] heroOrder;// 制造英雄的顺序
    int[] heroLife;
    int[] heroForce;
    ArrayList<Hero> heroList = new ArrayList<Hero>();// 制造的英雄

    Headquarter(int life_, String army_, int[] heroOrder_, int[] herolife_, int[] heroForce_) {
        life = life_;
        army = army_;
        heroOrder = heroOrder_;
        heroLife = herolife_;
        heroForce = heroForce_;
    }

    void createHero() {
        int det = heroOrder[wowTime % 5];
        if (stop || life < heroLife[det]) {
            stop = true;
            return;
        }
        life -= heroLife[det];
        Hero tmp;
        if (det == 0)
            tmp = new Dragon(heroLife[det], heroForce[det], wowTime + 1, army);
        else if (det == 1)
            tmp = new Ninja(heroLife[det], heroForce[det], wowTime + 1, army);
        else if (det == 2)
            tmp = new Iceman(heroLife[det], heroForce[det], wowTime + 1, army);
        else if (det == 3)
            tmp = new Lion(heroLife[det], heroForce[det], wowTime + 1, army, life, loyaltyDecrease);
        else
            tmp = new Wolf(heroLife[det], heroForce[det], wowTime + 1, army);
        if (army == "red")
            tmp.setMarchParam(0, 1, numCity + 1);
        else
            tmp.setMarchParam(numCity + 1, -1, 0);
        heroList.add(tmp);
    }

    void checkLion() {
        for (int i = 0; i < heroList.size(); i++) {
            if (heroList.get(i).heroType == "lion")
                ((Lion) heroList.get(i)).runAway();
        }
        checkHero();
    }

    void checkHero() {
        ArrayList<Hero> tmpList = new ArrayList<Hero>();
        for (Hero h : heroList)
            if (h.life > 0)
                tmpList.add(h);
        heroList = tmpList;
    }

    void marchHero() {
        for (Hero h : heroList)
            h.step();
    }

    void printLife() {
        if (wowTime < 10)
            System.out.print("00" + wowTime + ":");
        else if (wowTime < 100)
            System.out.print("0" + wowTime + ":");
        else
            System.out.print(wowTime + ":");
        System.out.println("50 " + life + " elements in " + army + " headquarter");
    }

    public static void main(String[] args) {
        int[] redHeroOrder = { 2, 3, 4, 1, 0 };
        int[] blueHeroOrder = { 3, 0, 1, 2, 4 };
        int[] heroLife = new int[5];
        int[] heroForce = new int[5];
        
        Scanner stdin = new Scanner(System.in);
        
        int initLife = stdin.nextInt();
        numCity = stdin.nextInt();
        loyaltyDecrease = stdin.nextInt();
        int maxWowTime = stdin.nextInt();

        for(int i=0;i<5;i++)
            heroLife[i] = stdin.nextInt();
        for(int i=0;i<5;i++)
            heroForce[i] = stdin.nextInt();
        stdin.close();

        Headquarter redHead = new Headquarter(initLife, "red", redHeroOrder, heroLife, heroForce);
        Headquarter blueHead = new Headquarter(initLife, "blue", blueHeroOrder, heroLife, heroForce);

        while (Headquarter.wowTime * 60 <= maxWowTime) {
            redHead.createHero();
            blueHead.createHero();

            if (Headquarter.wowTime * 60 + 5 > maxWowTime)
                break;
            redHead.checkLion();
            blueHead.checkLion();

            if (Headquarter.wowTime * 60 + 10 > maxWowTime)
                break;
            redHead.marchHero();
            blueHead.marchHero();
            ArrayList<Hero> tmpList = new ArrayList<Hero>();
            tmpList.addAll(redHead.heroList);
            tmpList.addAll(blueHead.heroList);
            Collections.sort(tmpList);
            for (Hero h : tmpList)
                h.printStep();
            if (Hero.wowEnd)
                break;
            if (Headquarter.wowTime * 60 + 35 > maxWowTime)
                break;
            for (int i = redHead.heroList.size() - 1; i >= 0; i--) {
                for (int j = 0; j < blueHead.heroList.size(); j++) {
                    if (redHead.heroList.get(i).heroType == "wolf")
                        ((Wolf) redHead.heroList.get(i)).stoleWeapon(blueHead.heroList.get(j));
                    if (blueHead.heroList.get(j).heroType == "wolf")
                        ((Wolf) blueHead.heroList.get(j)).stoleWeapon(redHead.heroList.get(i));
                }
            }

            if (Headquarter.wowTime * 60 + 40 > maxWowTime)
                break;
            for (int i = redHead.heroList.size() - 1; i >= 0; i--) {
                for (int j = blueHead.heroList.size() - 1; j >= 0; j--) {
                    if (redHead.heroList.get(i).city % 2 == 1)
                        redHead.heroList.get(i).fightWith(blueHead.heroList.get(j));
                    else
                        blueHead.heroList.get(j).fightWith(redHead.heroList.get(i));
                }
            }
            redHead.checkHero();
            blueHead.checkHero();

            if (Headquarter.wowTime * 60 + 50 > maxWowTime)
                break;
            redHead.printLife();
            blueHead.printLife();

            if (Headquarter.wowTime * 60 + 55 > maxWowTime)
                break;
            tmpList.clear();
            tmpList.addAll(redHead.heroList);
            tmpList.addAll(blueHead.heroList);
            Collections.sort(tmpList);
            for (Hero h : tmpList)
                h.printWeapon();

            Headquarter.wowTime++;
            // if(Headquarter.wowTime>5) break;
        }
    }
}