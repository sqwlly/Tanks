package sample.content.common;

public class Attribute {
    private int value; //初始值

    public int getMaxValue() {
        return maxValue;
    }

    private int maxValue; //最大值
    private int minValue; //最小值
    private int addBaseValue; //增加的基数量
    private int subBaseValue; //减少的基数量

    public Attribute(int value) {
        this(value, Integer.MAX_VALUE);
    }

    public Attribute(int value, int maxValue) {
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = 0;
        this.addBaseValue = 10;
        this.subBaseValue = 10;
    }

    public boolean health() {
        return value > minValue;
    }

    /**
     * @Description 按基数增加
     * @Param []
     * @return void
     */
    public void add() {
        this.add(this.addBaseValue);
    }

    /**
     * @Description 按指定值增加
     * @Param [addValue]
     * @return void
     */
    public void add(int addValue) {
        //大于最大值时等于最大值
        int i = this.value + addValue;
        this.value = Math.min(i, this.maxValue);
    }

    /**
     * @Description 设置属性为最大值
     * @Param []
     * @return void
     */
    public void toMax() {
        this.value = this.maxValue;
    }

    public void subtract() {
        this.subtract(this.subBaseValue);
    }

    public void subtract(int subtractValue) {
        //小于最小值时等于最小值
        int i = this.value - subtractValue;
        this.value = i < minValue ? minValue : i;
    }

    public void addMaxValue(int maxValue) {
        this.maxValue += maxValue;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setAddBaseValue(int addBaseValue) {
        this.addBaseValue = addBaseValue;
    }

    public void setSubBaseValue(int subtractBaseValue) {
        this.subBaseValue = subtractBaseValue;
    }

    /**
     * @Description 增加基数与减小基数相同时
     * @Param [baseValue]
     * @return sample.content.common.Attribute
     */
    public void setSameBaseValue(int baseValue) {
        this.addBaseValue = baseValue;
        this.subBaseValue = baseValue;
    }

    public int getValue() {
        return value;
    }
}
