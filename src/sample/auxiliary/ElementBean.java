package sample.auxiliary;

import sample.auxiliary.service.EnemyElementService;
import sample.auxiliary.service.PlayerElementService;
import sample.auxiliary.service.SubstanceElementService;
import sample.base.IBaseService;

public enum ElementBean {
    Player(new PlayerElementService()),
    Enemy(new EnemyElementService()),
    Substance(new SubstanceElementService());

    ElementBean(IBaseService service) {
        this.service = service;
    }

    private IBaseService service;

    public IBaseService getService() {
        return service;
    }

    public static void init() {
        Constant.TIMER_STOP_ON_OFF = false;//初始化timer 开关
        for (ElementBean bean : ElementBean.values()) {
            bean.getService().init();
        }
    }
}
