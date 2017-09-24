package com.training.android.undivided.Group.Model;

public class GroupModel {

    private String GroupName;
    private String GroupDesc;
    private String GroupMessage;
    private int Rule1;
    private int Rule2;
    private int Rule3;
    private int Rule4;
    private int Rule5;
    private int Rule6;
    private int Rule7;

    public GroupModel() {
    }

    public GroupModel(String groupName, String groupDesc, String groupMessage,
                      int rule1, int rule2, int rule3, int rule4, int rule5, int rule6, int rule7) {
        GroupName = groupName;
        GroupDesc = groupDesc;
        GroupMessage = groupMessage;
        Rule1 = rule1;
        Rule2 = rule2;
        Rule3 = rule3;
        Rule4 = rule4;
        Rule5 = rule5;
        Rule6 = rule6;
        Rule7 = rule7;
    }

    public String getGroupName() {
        return GroupName;
    }

    public String getGroupDesc() {
        return GroupDesc;
    }

    public String getGroupMessage() {
        return GroupMessage;
    }

    public int getRule1() {
        return Rule1;
    }

    public int getRule2() {
        return Rule2;
    }

    public int getRule3() {
        return Rule3;
    }

    public int getRule4() {
        return Rule4;
    }

    public int getRule5() {
        return Rule5;
    }

    public int getRule6() {
        return Rule6;
    }

    public int getRule7() {
        return Rule7;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public void setGroupDesc(String groupDesc) {
        GroupDesc = groupDesc;
    }

    public void setGroupMessage(String groupMessage) {
        GroupMessage = groupMessage;
    }

    public void setRule1(int rule1) {
        Rule1 = rule1;
    }

    public void setRule2(int rule2) {
        Rule2 = rule2;
    }

    public void setRule3(int rule3) {
        Rule3 = rule3;
    }

    public void setRule4(int rule4) {
        Rule4 = rule4;
    }

    public void setRule5(int rule5) {
        Rule5 = rule5;
    }

    public void setRule6(int rule6) {
        Rule6 = rule6;
    }

    public void setRule7(int rule7) {
        Rule7 = rule7;
    }
}
