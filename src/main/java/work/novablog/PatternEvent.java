package work.novablog;

/**
 * パターンキャンバスイベントを実装するためのインターフェース
 */
public interface PatternEvent {

	/**
	 * パターンを作成し終えたときの処理
	 */
	void create_pattern();

	/**
	 * パスワードを間違ったときの処理
	 * @param remaining 残り試行可能回数
	 */
	void wrong_passwd(int remaining);

	/**
	 * 作成終了処理
	 * @param passwd パスワード
	 */
	void create_end(String passwd);

	/**
	 * 認証終了処理
	 * @param ok 認証成功したか(true=成功)
	 */
	void auth_end(boolean ok);
}
