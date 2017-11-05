package common;

import java.io.Serializable;

public class Request implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Integer codeRequest;
	private Object data;

	public Request( Integer code, Object data ){
		this.setCodeRequest(code);
		this.setData(data);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getCodeRequest() {
		return codeRequest;
	}

	public void setCodeRequest(Integer codeRequest) {
		this.codeRequest = codeRequest;
	}
}
