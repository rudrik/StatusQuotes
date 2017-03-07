package com.Rudrik.StatusQuotes.pojo;

import android.graphics.Bitmap;

public class MenuDo {
	private Bitmap image;
	private String title;

	public MenuDo(Bitmap bit, String str) {
		setTitle(str);
		setImage(bit);
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap bitmap) {
		this.image = bitmap;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
