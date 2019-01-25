package com.pchome.akbadm.factory.malice;


public class MaliceFactory {
    private AbstractMalice malice5;
    private AbstractMalice malice6;
    private AbstractMalice malice7;
    private AbstractMalice malice8;

    public AbstractMalice getInstance(EnumMaliceType enumMaliceType) {
        AbstractMalice malice = null;

        switch(enumMaliceType) {
        case OVER_HOUR_LIMIT:
            malice = malice5;
            break;
        case OVER_PADDING:
            malice = malice6;
            break;
        case INSIDE_RECTANGLE:
            malice = malice7;
            break;
        case OVER_PRICE:
            malice = malice8;
            break;
        default:
            malice = null;
            break;
        }

        return malice;
    }

    public void setMalice5(AbstractMalice malice5) {
        this.malice5 = malice5;
    }

    public void setMalice6(AbstractMalice malice6) {
        this.malice6 = malice6;
    }

    public void setMalice7(AbstractMalice malice7) {
        this.malice7 = malice7;
    }

    public void setMalice8(AbstractMalice malice8) {
        this.malice8 = malice8;
    }
}
