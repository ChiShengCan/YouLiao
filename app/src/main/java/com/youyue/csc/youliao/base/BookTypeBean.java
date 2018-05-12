package com.youyue.csc.youliao.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by csc on 2018/5/12.
 * explain：
 */
public class BookTypeBean implements Parcelable {

    /**
     * code : 10000
     * msg : 请求成功
     * data : {"male":[{"icon":"/images/icon/玄幻.png","monthlyCount":14297,"bookCount":491899,"name":"玄幻"},{"icon":"/images/icon/奇幻.png","monthlyCount":1480,"bookCount":45889,"name":"奇幻"},{"icon":"/images/icon/武侠.png","monthlyCount":1006,"bookCount":40716,"name":"武侠"},{"icon":"/images/icon/仙侠.png","monthlyCount":5795,"bookCount":131973,"name":"仙侠"},{"icon":"/images/icon/都市.png","monthlyCount":10424,"bookCount":350719,"name":"都市"},{"icon":"/images/icon/职场.png","monthlyCount":671,"bookCount":16152,"name":"职场"},{"icon":"/images/icon/历史.png","monthlyCount":2281,"bookCount":69622,"name":"历史"},{"icon":"/images/icon/军事.png","monthlyCount":1102,"bookCount":14804,"name":"军事"},{"icon":"/images/icon/游戏.png","monthlyCount":1967,"bookCount":80173,"name":"游戏"},{"icon":"/images/icon/竞技.png","monthlyCount":243,"bookCount":5612,"name":"竞技"},{"icon":"/images/icon/科幻.png","monthlyCount":1764,"bookCount":116004,"name":"科幻"},{"icon":"/images/icon/灵异.png","monthlyCount":2783,"bookCount":37214,"name":"灵异"},{"icon":"/images/icon/同人.png","monthlyCount":339,"bookCount":38679,"name":"同人"},{"icon":"/images/icon/轻小说.png","monthlyCount":388,"bookCount":4768,"name":"轻小说"}],"female":[{"icon":"/images/icon/古代言情.png","monthlyCount":9247,"bookCount":459198,"name":"古代言情"},{"icon":"/images/icon/现代言情.png","monthlyCount":15686,"bookCount":587782,"name":"现代言情"},{"icon":"/images/icon/青春校园.png","monthlyCount":2600,"bookCount":115176,"name":"青春校园"},{"icon":"/images/icon/纯爱.png","monthlyCount":1089,"bookCount":132967,"name":"纯爱"},{"icon":"/images/icon/玄幻奇幻.png","monthlyCount":458,"bookCount":126395,"name":"玄幻奇幻"},{"icon":"/images/icon/武侠仙侠.png","monthlyCount":1294,"bookCount":62975,"name":"武侠仙侠"},{"icon":"/images/icon/科幻.png","monthlyCount":253,"bookCount":9638,"name":"科幻"},{"icon":"/images/icon/游戏竞技.png","monthlyCount":139,"bookCount":6509,"name":"游戏竞技"},{"icon":"/images/icon/悬疑灵异.png","monthlyCount":568,"bookCount":13585,"name":"悬疑灵异"},{"icon":"/images/icon/同人.png","monthlyCount":160,"bookCount":118859,"name":"同人"},{"icon":"/images/icon/女尊.png","monthlyCount":875,"bookCount":20306,"name":"女尊"},{"icon":"/images/icon/莉莉.png","monthlyCount":72,"bookCount":25882,"name":"莉莉"}],"press":[{"icon":"/images/icon/传记名著.png","monthlyCount":1387,"bookCount":4280,"name":"传记名著"},{"icon":"/images/icon/出版小说.png","monthlyCount":2433,"bookCount":10610,"name":"出版小说"},{"icon":"/images/icon/人文社科.png","monthlyCount":8912,"bookCount":54617,"name":"人文社科"},{"icon":"/images/icon/生活时尚.png","monthlyCount":522,"bookCount":2731,"name":"生活时尚"},{"icon":"/images/icon/经管理财.png","monthlyCount":2129,"bookCount":5336,"name":"经管理财"},{"icon":"/images/icon/青春言情.png","monthlyCount":1145,"bookCount":10289,"name":"青春言情"},{"icon":"/images/icon/外文原版.png","monthlyCount":394,"bookCount":1117,"name":"外文原版"},{"icon":"/images/icon/政治军事.png","monthlyCount":180,"bookCount":544,"name":"政治军事"},{"icon":"/images/icon/成功励志.png","monthlyCount":2208,"bookCount":9166,"name":"成功励志"},{"icon":"/images/icon/育儿健康.png","monthlyCount":2311,"bookCount":10218,"name":"育儿健康"}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    protected BookTypeBean(Parcel in) {
        code = in.readInt();
        msg = in.readString();
    }

    public static final Creator<BookTypeBean> CREATOR = new Creator<BookTypeBean>() {
        @Override
        public BookTypeBean createFromParcel(Parcel in) {
            return new BookTypeBean(in);
        }

        @Override
        public BookTypeBean[] newArray(int size) {
            return new BookTypeBean[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(msg);
    }

    public static class DataBean {
        private List<MaleBean> male;
        private List<FemaleBean> female;
        private List<PressBean> press;
        public List<CartoonBean> mCartoonList;

        public List<MaleBean> getMale() {
            return male;
        }

        public void setMale(List<MaleBean> male) {
            this.male = male;
        }

        public List<FemaleBean> getFemale() {
            return female;
        }

        public void setFemale(List<FemaleBean> female) {
            this.female = female;
        }

        public List<PressBean> getPress() {
            return press;
        }

        public void setPress(List<PressBean> press) {
            this.press = press;
        }

        public List<CartoonBean> getCartoon() {
            return mCartoonList;
        }

        public void setCartoon(List<CartoonBean> cartoonList) {
            this.mCartoonList = cartoonList;
        }

        public static class MaleBean {
            /**
             * icon : /images/icon/玄幻.png
             * monthlyCount : 14297
             * bookCount : 491899
             * name : 玄幻
             */

            private String icon;
            private int monthlyCount;
            private int bookCount;
            private String name;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getMonthlyCount() {
                return monthlyCount;
            }

            public void setMonthlyCount(int monthlyCount) {
                this.monthlyCount = monthlyCount;
            }

            public int getBookCount() {
                return bookCount;
            }

            public void setBookCount(int bookCount) {
                this.bookCount = bookCount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class FemaleBean {
            /**
             * icon : /images/icon/古代言情.png
             * monthlyCount : 9247
             * bookCount : 459198
             * name : 古代言情
             */

            private String icon;
            private int monthlyCount;
            private int bookCount;
            private String name;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getMonthlyCount() {
                return monthlyCount;
            }

            public void setMonthlyCount(int monthlyCount) {
                this.monthlyCount = monthlyCount;
            }

            public int getBookCount() {
                return bookCount;
            }

            public void setBookCount(int bookCount) {
                this.bookCount = bookCount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class PressBean {
            /**
             * icon : /images/icon/传记名著.png
             * monthlyCount : 1387
             * bookCount : 4280
             * name : 传记名著
             */

            private String icon;
            private int monthlyCount;
            private int bookCount;
            private String name;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getMonthlyCount() {
                return monthlyCount;
            }

            public void setMonthlyCount(int monthlyCount) {
                this.monthlyCount = monthlyCount;
            }

            public int getBookCount() {
                return bookCount;
            }

            public void setBookCount(int bookCount) {
                this.bookCount = bookCount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class CartoonBean {

            public String icon;
            public int monthlyCount;
            public int bookCount;
            public String name;


            public CartoonBean( String name,int bookCount) {
                this.bookCount = bookCount;
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getMonthlyCount() {
                return monthlyCount;
            }

            public void setMonthlyCount(int monthlyCount) {
                this.monthlyCount = monthlyCount;
            }

            public int getBookCount() {
                return bookCount;
            }

            public void setBookCount(int bookCount) {
                this.bookCount = bookCount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
