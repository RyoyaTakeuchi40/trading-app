package simplex.trading.menus;

public interface MenuCommand {

    /**
     * @return メニュー名
     */
    String menuName();

    /**
     * メニューを実行する
     *
     * @return メニュー選択を継続する場合はtrue, 終了する場合はfalse
     */
    boolean execute();

}
