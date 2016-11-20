package io.github.clendy.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity
 *
 * @author Clendy
 * @date 2016/11/16 016 13:49
 * @e-mail yc330483161@outlook.com
 */
public class Entity implements Parcelable {

    int id;
    String title;
    String pic_url;

    public Entity() {
    }

    public Entity(int id, String title, String pic_url) {
        this.id = id;
        this.title = title;
        this.pic_url = pic_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pic_url='" + pic_url + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.pic_url);
    }

    protected Entity(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.pic_url = in.readString();
    }

    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel source) {
            return new Entity(source);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };
}
