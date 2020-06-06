package WOW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Hero implements Comparable<Hero> {
    static boolean wowEnd = false;// 战斗是否结束
    int life;// 生命值
    int force;// 武力值
    int id;// 武士编号
    int city;// 所处城市
    int inc;// 前进方向
    int endCity;// 城市总数
    String heroType;// 武士种类
    String army;// 所属军团
    ArrayList<Weapon> weaponList = new ArrayList<Weapon>();// 武器列表

    Hero(int life_, int force_, int id_, String heroType_, String army_) {
        life = life_;
        force = force_;
        id = id_;
        heroType = heroType_;
        army = army_;
        ptintTime();
        System.out.println("00 " + this + " born");
    }

    void setMarchParam(int city_, int inc_, int endCity_) {
        city = city_;
        inc = inc_;
        endCity = endCity_;
    }

    void step() {
        city += inc;
    }

    void printStep() {
        if (city == endCity) {
            String lossArmy;
            if (army == "red")
                lossArmy = "blue";
            else
                lossArmy = "red";
            ptintTime();
            System.out.println("10 " + this + " reached " + lossArmy + " headquarter with " + life
                    + " elements and force " + force);
            ptintTime();
            System.out.println("10 " + lossArmy + " headquarter was taken");
            wowEnd = true;
            return;
        }
        ptintTime();
        System.out
                .println("10 " + this + " marched to city " + city + " with " + life + " elements and force " + force);
    }

    void fightWith(Hero h) {
        if (city != h.city)
            return;
        sortWeapon();
        h.sortWeapon();
        int round = 0;
        while (round < 100) {
            checkWeapon();
            h.checkWeapon();
            if (weaponList.size() > 0) {
                h.life -= weaponList.get(round % (weaponList.size())).usedBy(this);
            }
            if (h.weaponList.size() > 0 && h.life > 0) {
                life -= h.weaponList.get(round % (h.weaponList.size())).usedBy(h);
            }
            if (life <= 0 || h.life <= 0)
                break;
            round++;
        }
        ptintTime();
        if (life <= 0 && h.life > 0) {
            System.out
                    .println("40 " + h + " killed " + this + " in city " + city + " remaining " + h.life + " elements");
            h.captureWeaponFrom(this);
            if (h.heroType == "dragon") {
                ((Dragon) h).yell();
            }

        } else if (life > 0 && h.life <= 0) {
            System.out.println("40 " + this + " killed " + h + " in city " + city + " remaining " + life + " elements");
            captureWeaponFrom(h);
            if (heroType == "dragon") {
                ((Dragon) this).yell();
            }
        } else if (life > 0 && h.life > 0) {
            if (army == "red") {
                System.out.println("40 both " + this + " and " + h + " were alive in city " + city);
                if (heroType == "dragon") {
                    ((Dragon) this).yell();
                }
                if (h.heroType == "dragon") {
                    ((Dragon) h).yell();
                }
            } else {
                System.out.println("40 both " + h + " and " + this + " were alive in city " + city);
                if (h.heroType == "dragon") {
                    ((Dragon) h).yell();
                }
                if (heroType == "dragon") {
                    ((Dragon) this).yell();
                }
            }
        } else {
            if (army == "red")
                System.out.println("40 both " + this + " and " + h + " died in city " + city);
            else
                System.out.println("40 both " + h + " and " + this + " died in city " + city);
        }
    }

    @Override
    public String toString() {
        return army + " " + heroType + " " + id;
    }

    @Override
    public int compareTo(Hero h) {
        if (city < h.city)
            return -1;
        if (city > h.city)
            return 1;
        if (army == "red")
            return -1;
        return 1;
    }

    void captureWeaponFrom(Hero h) {
        int i = 0;
        while (weaponList.size() <= 10 && i < h.weaponList.size()) {
            weaponList.add(h.weaponList.get(i++));
        }
    }

    void checkWeapon() {
        ArrayList<Weapon> tmpList = new ArrayList<Weapon>();
        for (Weapon wp : weaponList)
            if (wp.isExisted())
                tmpList.add(wp);
        weaponList = tmpList;
    }

    void sortWeapon() {
        Collections.sort(weaponList);
    }

    void printWeapon() {
        int[] cnt = { 0, 0, 0 };
        for (Weapon wp : weaponList) {
            if (wp.isExisted())
                cnt[wp.getWeaponId()]++;
        }
        ptintTime();
        System.out.println("55 " + this + " has " + cnt[0] + " sword " + cnt[1] + " bomb " + cnt[2] + " arrow and "
                + life + " elements");
    }

    static Weapon getWeapon(int id_) {
        if (id_ == 0)
            return new Sword(0, "sword");
        if (id_ == 1)
            return new Bomb(1, "bomb");
        return new Arrow(2, "arrow");
    }

    void ptintTime() {
        int wowTime = Headquarter.wowTime;
        if (wowTime < 10)
            System.out.print("00" + wowTime + ":");
        else if (wowTime < 100)
            System.out.print("0" + wowTime + ":");
        else
            System.out.print(wowTime + ":");
    }

    public static void main(String[] args) {
        Hero h = new Hero(100, 20, 123, "dragon", "red");
        System.out.println(h.weaponList.size());
    }
}

class Dragon extends Hero {
    Dragon(int life_, int force_, int id_, String army_) {
        super(life_, force_, id_, "dragon", army_);
        weaponList.add(getWeapon(id % 3));
    }

    void yell() {
        ptintTime();
        System.out.println("40 " + this + " yelled in city " + city);
    }
}

class Ninja extends Hero {
    Ninja(int life_, int force_, int id_, String army_) {
        super(life_, force_, id_, "ninja", army_);
        weaponList.add(getWeapon(id % 3));
        weaponList.add(getWeapon((id + 1) % 3));
    }
}

class Iceman extends Hero {
    Iceman(int life_, int force_, int id_, String army_) {
        super(life_, force_, id_, "iceman", army_);
        weaponList.add(getWeapon(id % 3));
    }

    @Override
    void step() {
        city += inc;
        life -= 10 * life / 100;
    }
}

class Lion extends Hero {
    private int loyalty;// 忠诚度
    private int loyaltyDecrease;// 每走一步，忠诚度降低的值

    Lion(int life_, int force_, int id_, String army_, int loyalty_, int loyaltyDecrease_) {
        super(life_, force_, id_, "lion", army_);
        loyalty = loyalty_;
        loyaltyDecrease = loyaltyDecrease_;
        weaponList.add(getWeapon(id % 3));
        System.out.println("Its loyalty is " + loyalty);
    }

    @Override
    void step() {
        city += inc;
        loyalty -= loyaltyDecrease;
    }

    void runAway() {
        if (loyalty <= 0) {
            life = -1;
            ptintTime();
            System.out.println("05 " + this + " ran away");
        }
    }
}

class Wolf extends Hero {
    Wolf(int life_, int force_, int id_, String army_) {
        super(life_, force_, id_, "wolf", army_);
    }

    void stoleWeapon(Hero h) {
        checkWeapon();
        h.checkWeapon();
        if (city != h.city || h.heroType == "wolf" || h.weaponList.isEmpty())
            return;
        h.sortWeapon();
        Weapon stoleType = h.weaponList.get(0);
        int minId = stoleType.getWeaponId(), cnt = 0;
        Iterator<Weapon> itWeapon = h.weaponList.iterator();
        while (itWeapon.hasNext() && weaponList.size() <= 10) {
            Weapon tmpWeapon = itWeapon.next();
            if (tmpWeapon.getWeaponId() == minId) {
                itWeapon.remove();
                weaponList.add(tmpWeapon);
                cnt++;
            }
            else break;
        }
        ptintTime();
        System.out.println(
                "35 " + this + " took " + cnt + " " + stoleType.getWeaponType() + " from " + h + " in city " + city);
    }
}
