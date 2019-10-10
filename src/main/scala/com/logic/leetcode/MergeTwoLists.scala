package com.logic.leetcode

//Definition for singly-linked list.
case class ListNode(var _x: Int = 0) {
  var next: ListNode = _
  var x: Int = _x

  override def toString: String = {
    if (next != null) {
      s"$x->${next.toString}"
    } else {
      x.toString
    }
  }
}

object MergeTwoLists {


  def main(args: Array[String]): Unit = {
    val n=ListNode()
    val n1 = ListNode(4)
    val n2 = ListNode(2)
    n2.next = n1
    val n3 = ListNode(1)
    n3.next = n2
    println(n3)

    val n4 = ListNode(4)
    val n5 = ListNode(3)
    n5.next = n4
    val n6 = ListNode(1)
    n6.next = n5
    println(n6)

    println(mergeTwoLists(n3, n6))
    println(mergeTwoLists(null, null))
  }

  def mergeTwoLists(l1: ListNode, l2: ListNode): ListNode = {
    if (l1==null && l2==null){
      null
    } else if (l1 == null && l2 != null) {
      l2
    } else if (l1 != null && l2 == null) {
      l1
    } else {
      var a1 = l1
      var a2 = l2
      val header = if (l1.x < l2.x) {
        a1 = a1.next
        l1
      } else {
        a2 = a2.next
        l2
      }
      var tail = header
      // println(s"header:${header.x}")
      while (a1 != null && a2 != null) {
        // println(s"a1:${a1.x},a2:${a2.x}")
        if (a1.x < a2.x) {
          tail.next = a1
          tail=a1
          a1 = a1.next
        } else {
          tail.next = a2
          tail=a2
          a2 = a2.next
        }
      }
      if (a1 != null) {
        tail.next = a1
      } else {
        tail.next = a2
      }
      header
    }
  }


}
