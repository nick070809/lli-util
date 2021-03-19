package unit.zijie;

import org.junit.Test;

import java.util.Arrays;

/**
 * Description ： https://leetcode-cn.com/problemset/all/
 * Created by  xianguang.skx
 * Since 2021/1/5
 */

public class Week1 {

    //order 1

    @Test
    public void testOrder1() {
        int[] nums = {2, 7, 11, 15};
        int target = 17;
        System.out.println(Arrays.toString(twoSum(nums, target)));
    }

    public int[] twoSum(int[] nums, int target) {
        int x = 0;
        int y = 0;
        flag:
        for (int index = 0; index < nums.length; index++) {
            for (int ydex = 0; ydex < nums.length; ydex++) {
                if (index == ydex) {
                    continue;
                }
                if (nums[index] + nums[ydex] == target) {
                    x = index;
                    y = ydex;
                    break flag;
                }
            }
        }
        int[] a = {x, y};
        return a;
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //进位
        int entry = 0;
        ListNode node = new ListNode();
        ListNode currnetNode = node ;
        while(l1 != null || l2 != null || entry != 0){
            int value_ = (l1==null?0:l1.val)  + (l2==null?0:l2.val)  + entry ;
            entry = value_ /10;
            //剩余值
            value_ = value_ %10 ;

            currnetNode.next = new ListNode();
            currnetNode.next.val =value_ ; // 确保下一位有值
            currnetNode =  currnetNode.next ;

            l1 = l1==null?null:l1.next ;
            l2 = l2==null?null:l2.next ;

        }
        return  node.next ;
    }




    public int lengthOfLongestSubstring(String s) {


        if(s == null || s.length() ==0){
            return  0;
        }
        int res = 0;

        //ASCII == 127 s == xiangu
        //记录上次出现的位置, 默认为不存在
        int[] lastLocations = new int[128];
        for(int location : lastLocations){
            location = -1 ;
        }

        int size = s.length() ;
        int maxLength = 0; //前两次出现最大间隔数量   abcabcdabcde 11
        for(int i =0 ;i <size ;i++){
            //获取码
            int code_ = s.charAt(i);
            //获取上一次位置 lastLocations[code_]
            maxLength =  Math.max(maxLength,  lastLocations[code_]);  //0,0,
            res = Math.max(res,(i+1) - maxLength ) ;  //
            lastLocations[code_] = i ;//0,3,7
        }
        return res;

    }

    class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
