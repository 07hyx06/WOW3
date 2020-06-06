package WOW;

public class Weapon implements Comparable<Weapon> {
    private boolean exist = true;// 武器是否存在
    private int id;// 武器编号
    private String weaponType;// 武器种类

    Weapon(int id_, String weaponType_) {
        id = id_;
        weaponType = weaponType_;
    }

    @Override
    public int compareTo(Weapon w) {
        if (id < w.id)
            return -1;
        if (id > w.id)
            return 1;
        if (w instanceof Arrow) {
            if (((Arrow) this).getUsedTime() > ((Arrow) w).getUsedTime())
                return -1;
            return 1;
        }
        return 0;
    }

    void setExisted(boolean b) {
        exist = b;
    }

    boolean isExisted() {
        return exist;
    }

    int getWeaponId() {
        return id;
    }

    String getWeaponType() {
        return weaponType;
    }

    int usedBy(Hero h) {
        return 20 * h.force / 100;
    }
}

class Sword extends Weapon {
    Sword(int id_, String weaponType_) {
        super(id_, weaponType_);
    }
}

class Bomb extends Weapon {
    Bomb(int id_, String weaponType_) {
        super(id_, weaponType_);
    }

    @Override
    int usedBy(Hero h) {
        int force = 40 * h.force / 100;
        if (!(h instanceof Ninja))
            h.life -= force / 2;
        setExisted(false);
        return force;
    }
}

class Arrow extends Weapon {
    private int usedTime = 0;

    Arrow(int id_, String weaponType_) {
        super(id_, weaponType_);
    }

    @Override
    int usedBy(Hero h) {
        usedTime++;
        if (usedTime == 2)
            setExisted(false);
        return 30 * h.force / 100;
    }

    int getUsedTime() {
        return usedTime;
    }
}
