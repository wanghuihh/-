# Quickly-find-contacts
快速定位联系人及可以直接用的拼音工具-Android
效果图

1:自定义view实现构造QuickIndex extends View{}
2:准备字母
private String[] letterArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U","V", "W", "X", "Y", "Z"};
3:准备画笔画出背景和文字:  1:画笔;2:ondraw();3:位置逻辑;
paint=new Paint(paint.ANTI_ALIAS_FLAG);//构造方法中初始化//抗锯齿
paint.setColor(ColorDefault);
int textSize = getResources().getDimensionPixelSize(R.dimen.text_size);
paint.setTextSize(textSize);
//由于文字绘制的起点默认是左下角，
paint.setTextAlign(Paint.Align.CENTER);//设置起点为底边的中心，
//一定用float来保证精度  
float cellHeight;
@Override
protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    cellHeight = getMeasuredHeight() * 1f / letterArr.length;//格子的高度
}
@Override
protected void onDraw(Canvas canvas) {
    //super.onDraw(canvas);
    for (int i = 0; i < letterArr.length; i++) {
        String text =letterArr[i];
        float x = getMeasuredWidth() / 2;//当前view宽度的一半;
        float y = getTextHeight(text) / 2 + cellHeight / 2 + i * cellHeight;
        canvas.drawText(text,x,y,paint);
    }

}
//文字的高度方法提取
private int getTextHeight(String text) {
    Rect bounds=new Rect();//矩形边框
    paint.getTextBounds(text,0,text.length(),bounds);
    return  bounds.height();
}
4:实现触摸时log出现那个字母
public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()){
    case MotionEvent.ACTION_DOWN:
    case MotionEvent.ACTION_MOVE:
    //使用y坐标除以格子的高，得到的是字母的索引
    int current = (int) (event.getY() /cellHeight);
    if(current!=index){
        index = current;

        //对代码进行安全性的检查
        if(index>=0 && index<letterArr.length){
            String letter = letterArr[index];
            if(listener!=null){
                listener.onLetterChange(letter);
            }
            //Log.e("***************",letter);
        }
    }
    break;
    case MotionEvent.ACTION_UP:
        index = -1;
        if(listener!=null){
            listener.onRelease();
        }

        break;
    }
    invalidate();
    return  true;invalidate(); //重绘调用ondraw()方法invalidate(); 
5:实现选中的时候颜色改变抬起的时候恢复   
当 MotionEvent.ACTION_UP后: 重绘调用ondraw()方法invalidate(); 
在ondraw()做判断paint.setColor(index==i?Colorpressed:ColorDefault);
6:做回调接口在滑动时候调用暴露给外界方法  实现调用功能能;
if(listener!=null){
    listener.onLetterChange(letter);
}
//回调方法
private OnLetterChangeListener listener;

public void setOnLetterChangeListener(OnLetterChangeListener listener) {
    this.listener = listener;
}

public interface OnLetterChangeListener{
    void onLetterChange(String letter);
    //当抬起的时候执行
    void onRelease();
}
7:准备listview数据(在控件中添加listview存放)ArrayList<Friend> friends = new ArrayList<>();friends.add(new Friend("李伟"));//省略部分
7.1设置数据这里用compile 'com.zhy:base-adapter:3.0.3'
listview.setAdapter(new FriendAdapter(this,R.layout.adapter_friend, friends));//设置adapter数据
//编写adapter
public class FriendAdapter extends CommonAdapter<Friend> {
    public FriendAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }
@Override
    protected void convert(ViewHolder viewHolder, Friend item, int position) {
        viewHolder.setText(R.id.tv_name, item.name);
    }
}
8:拼音工具:compile files('libs/pinyin4j-2.5.0.jar')

public class PinYinUtil { /**
 * 获取汉字对应的拼音
 * @param chinese
 * @return
 */
public static String getPinYin(String chinese){
    if(TextUtils.isEmpty(chinese)) return null;

    //控制拼音的格式化的
    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//大写字母
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不需要声调

    //由于pinyin4j只能支持对单个汉字查，不能对词语
    StringBuilder builder = new StringBuilder();
    char[] chars = chinese.toCharArray();
    for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        //1.过滤空格,如果是空格，直接忽略，继续下次
        if(Character.isWhitespace(c)){
            continue;
        }

        //2.判断字符是否是汉字，在u8中，一个汉字3个字节，一个字节范围是-128~127；
        //所以汉字肯定大于127
        if(c > 127){
            //说明有可能是汉字,我们则尝试获取它对应的拼音
            try {
                //返回数组是因为多音字的存在，比如单：[dan, chan, shan]
                String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if(pinyinArr!=null){
                    //我们暂且只能用第1个,原因是，大部分汉字只有一个拼音，对于多音字我们只取第1个，这样
                    //可能无法精确判断拼音的读取。如果真想实现精确获取一个汉字的拼音，需要以下技术：
                    //1.分词技术        2.庞大的数据库支持
                    builder.append(pinyinArr[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //说明不是汉字所以获取不到拼音
            }
        }else {
            //小于127的肯定是asicc码表中的字符，很可能就是英文字母
            //我们选择直接拼接
            builder.append(c);
        }
    }
    return builder.toString();
    }
}
9://对数据进行排序
Collections.sort(friends);
10:实现回调借口:
quickIndexView.setOnLetterChangeListener(new QuickIndex.OnLetterChangeListener() {
    @Override
    public void onLetterChange(String letter) {
        //根据当前触摸的字母，去列表中查找首字母和letter一样的条目，找到后将其置顶
        for (int i = 0; i < friends.size(); i++) {
            String s = friends.get(i).pinyin.charAt(0) + "";
            if (s.equals(letter)) {
                //说明找到了，将其置顶
                listview.setSelection(i);
                //找到一次赶紧break
                    break;
                }
        }
        //显示出当前触摸的字母
        showCurrent(letter);
    }

    @Override
    public void onRelease() {
        tvCurrent.setVisibility(View.GONE);
    }
});
//展示字母方法
private void showCurrent(String letter) {
    tvCurrent.setText(letter);
    tvCurrent.setVisibility(View.VISIBLE);
}
<TextView
    android:id="@+id/tv_current"
    android:visibility="gone"
    android:layout_centerInParent="true"
    android:textColor="#fff"
    android:textSize="50sp"
    android:gravity="center"
    android:background="@drawable/bg"
    android:layout_width="110dp"
    android:layout_height="110dp" />
//shape
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">

    <solid android:color="#8000"/>
</shape>
