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


    public GroupModel() {
    }

    public GroupModel(String groupName, String groupDesc, String groupMessage,
                      int rule1, int rule2, int rule3, int rule4) {
        GroupName = groupName;
        GroupDesc = groupDesc;
        GroupMessage = groupMessage;
        Rule1 = rule1;
        Rule2 = rule2;
        Rule3 = rule3;
        Rule4 = rule4;

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

}
