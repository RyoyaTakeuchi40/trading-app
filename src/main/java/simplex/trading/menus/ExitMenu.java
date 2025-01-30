package simplex.trading.menus;


public class ExitMenu extends AbstractMenu {

    public ExitMenu() {
        super("アプリケーションを終了する");
    }

    @Override
    public boolean executeInternal() {
        System.out.println("アプリケーションを終了します。");
        return false;
    }
}
