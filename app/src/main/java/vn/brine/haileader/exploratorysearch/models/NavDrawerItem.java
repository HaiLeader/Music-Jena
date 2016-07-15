package vn.brine.haileader.exploratorysearch.models;

/**
 * Created by HaiLeader on 7/12/2016.
 */
public class NavDrawerItem {
//    private boolean showNotify;
    private String title;
    private int icon;
    private String count = "0";
    private boolean isCounterVisible = false;

    public NavDrawerItem() {}

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count) {
//        this.showNotify = showNotify;
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

//    public boolean isShowNotify() {
//        return showNotify;
//    }

//    public void setShowNotify(boolean showNotify) {
//        this.showNotify = showNotify;
//    }

    public String getTitle() {
        return title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
