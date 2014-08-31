/**
 * this interface exists just to allow the WebserviceHelper to make callback.
 */

package com.helper;

public interface RequestReceiver {
	void requestFinished(String result);

	//boolean onKeyPreIme(int keyCode, KeyEvent event);


}
