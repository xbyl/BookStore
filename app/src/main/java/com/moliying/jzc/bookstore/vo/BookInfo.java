package com.moliying.jzc.bookstore.vo;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * descreption:
 * company: moliying.com
 * Created by vince on 16/07/13.
 */
public class BookInfo extends BmobObject implements Parcelable {

    private String categoryId;//类别ID
    private String catalog;//目录
    private String authorDescription;// 作者描述
    private String contentDescription;// 内容描述
    private int total;// 库存数量
    private double discount;// 折扣率
    private double discountPrice;// 折扣价
    private double price;//定价
    private String publishingTime;//出版时间
    private String publishingCompany;//出版社
    private String author;//作者
    private String bookName;//书名
    private int star;//星级
    private BmobFile bookImage;//图
    private String ISBN; //ISBN
    private int revision;//版次
    private int pages;//页数
    private int NumberOfWords;//字数
    private String folio;//开本
    private String paper;//纸张
    private String packing;//包装
    private int impression;//印次
    private String mBookImagepath;

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public int getImpression() {
        return impression;
    }

    public void setImpression(int impression) {
        this.impression = impression;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getNumberOfWords() {
        return NumberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        NumberOfWords = numberOfWords;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorDescription() {
        return authorDescription;
    }

    public void setAuthorDescription(String authorDescription) {
        this.authorDescription = authorDescription;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPublishingCompany() {
        return publishingCompany;
    }

    public void setPublishingCompany(String publishingCompany) {
        this.publishingCompany = publishingCompany;
    }

    public String getPublishingTime() {
        return publishingTime;
    }

    public void setPublishingTime(String publishingTime) {
        this.publishingTime = publishingTime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public BmobFile getBookImage() {
        return bookImage;
    }

    public void setBookImage(BmobFile bookImage) {
        this.bookImage = bookImage;
    }

    public void setBookImagepath(String bookImagepath) {
        mBookImagepath = bookImagepath;
    }

    public String getBookImagepath() {
        return mBookImagepath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.catalog);
        dest.writeString(this.authorDescription);
        dest.writeString(this.contentDescription);
        dest.writeInt(this.total);
        dest.writeDouble(this.discount);
        dest.writeDouble(this.discountPrice);
        dest.writeDouble(this.price);
        dest.writeString(this.publishingTime);
        dest.writeString(this.publishingCompany);
        dest.writeString(this.author);
        dest.writeString(this.bookName);
        dest.writeInt(this.star);
        dest.writeSerializable(this.bookImage);
        dest.writeString(this.ISBN);
        dest.writeInt(this.revision);
        dest.writeInt(this.pages);
        dest.writeInt(this.NumberOfWords);
        dest.writeString(this.folio);
        dest.writeString(this.paper);
        dest.writeString(this.packing);
        dest.writeInt(this.impression);
        dest.writeString(this.mBookImagepath);
    }

    public BookInfo() {
    }

    protected BookInfo(Parcel in) {
        this.categoryId = in.readString();
        this.catalog = in.readString();
        this.authorDescription = in.readString();
        this.contentDescription = in.readString();
        this.total = in.readInt();
        this.discount = in.readDouble();
        this.discountPrice = in.readDouble();
        this.price = in.readDouble();
        this.publishingTime = in.readString();
        this.publishingCompany = in.readString();
        this.author = in.readString();
        this.bookName = in.readString();
        this.star = in.readInt();
        this.bookImage = (BmobFile) in.readSerializable();
        this.ISBN = in.readString();
        this.revision = in.readInt();
        this.pages = in.readInt();
        this.NumberOfWords = in.readInt();
        this.folio = in.readString();
        this.paper = in.readString();
        this.packing = in.readString();
        this.impression = in.readInt();
        this.mBookImagepath = in.readString();
    }

    public static final Parcelable.Creator<BookInfo> CREATOR = new Parcelable.Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
}
