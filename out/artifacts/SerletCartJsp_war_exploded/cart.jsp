<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"%>
<%@ page import="entity.Cart" %>
<%@ page import="entity.Items" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'cart.jsp' starting page</title>
 	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <link type="text/css" rel="stylesheet" href="css/style1.css" />
	  <script src="http://cdn.static.runoob.com/libs/jquery/1.10.2/jquery.min.js"></script>
    <script language="javascript">
	    function delcfm() {
	        if (!confirm("确认要删除？")) {
	            window.event.returnValue = false;
	        }
	    }

		/**
		*购买数量减一
		*/
	    function sub(id) {
	        var number = document.getElementById("number"+id).value;
	        if(number>=2)$.ajax({
                type: "POST",
                url: "cart",
                data: {
                    id: id,
                    action: "sub",
                },
                dataType: "json",
                success: function (data) {
                    document.getElementById("price"+id).innerHTML = data["allPrice"];//更新单个商品的总价
                    document.getElementById("total").innerHTML = "总计："+data["calTotalPrice"]+"￥";//更新总价
                    document.getElementById("number"+id).value = parseInt(data["num"]);//单个商品的购买个数
                },
            });else {
	            alert("最小单位为1");
			}
        }

		/**
		*购买数量加一
		*/
        function add(id) {
	        var number = document.getElementById("number"+id).value;
	        if(number<999)$.ajax({
				type: "POST",
				url : "cart",
				data: {
				    id : id,
					action : "addNum"
				},
				dataType: "json",
				success:function (data) {
					document.getElementById("price"+id).innerHTML = data["allPrice"];//更新单个商品的总价
                    document.getElementById("total").innerHTML = "总计："+data["calTotalPrice"]+"￥";//更新总价
                    document.getElementById("number"+id).value = parseInt(data["num"]);//单个商品的购买个数
                }
	        });
	        else {
	            alert("最大单位为999");
			}
        }
		/**
		*用于判断用户输入的数字是否为正整数
		* @param 用户输入数字之前的值，用于数据回滚
		*/
        function judge(numValue) {
			var number = document.getElementById("number").value;
			if(number<1){
	            alert("最小单位为1");
	            document.getElementById("number").value = 1;
			}
			if(number>999){
	            alert("最大单位为999");
	            document.getElementById("number").value = 999;
			}
			if(!isDot(number)){
			    alert("请输入一个正整数");
			    document.getElementById("number").value = numValue;
			}
        }

		/**
		*这是一个用原生JS现实的Ajax请求，实现对多种数据的接受略显复杂，建议采用jquery+json
		* @param id商品信息的id
		* @param action 对购物车的操作
		*/
        function postRequest(id ,action) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST","cart");
			var date ="id="+id+"&action="+action;
			xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			xhr.send(date);
			xhr.onreadystatechange = function () {
				// 这步为判断服务器是否正确响应
			  if (xhr.readyState == 4 && xhr.status == 200) {
			      var t = xhr.responseText;
				document.getElementById("number").value =t;
			  }
			  else {
			      return null;
			  }
			}
        }

		/**
		*
		* @param 处理判断是否为合法的正整数函数
		* @returns {boolean}
		*/
        function isDot(val) {
	        if(val === "" || val ==null){//是否为空
				return false;
			}
			//是否为数字
			if(!isNaN(val)){
			    var result = (val.toString()).indexOf(".");
			    //是否包含小数点，找不到result为-1
			    if(result == -1) {//没找到小数点，是想要的数字
			        return true;
				} else {
					return false;
				}
			}else{
				return false;
			}
		}
   </script>
  </head>
  
  <body>
   <h1>我的购物车</h1>
   <a href="index.jsp">首页</a> >> <a href="index.jsp">商品列表</a>
   <hr> 
   <div id="shopping">
   <form action="" method="">		
			<table>
				<tr>
					<th>商品名称</th>
					<th>商品单价</th>
					<th>商品价格</th>
					<th>购买数量</th>
					<th>操作</th>
				</tr>
				<% 
				   //首先判断session中是否有购物车对象
				   if(request.getSession().getAttribute("cart")!=null)
				   {
				%>
				<!-- 循环的开始 -->
				     <% 
				         Cart cart = (Cart)request.getSession().getAttribute("cart");
				         HashMap<Items,Integer> goods = cart.getGoods();
				         Set<Items> items = goods.keySet();
				         Iterator<Items> it = items.iterator();
				         
				         while(it.hasNext())
				         {
				            Items i = it.next();
				     %> 
				<tr name="products" id="product_id_1">
					<td class="thumb"><img src="images/<%=i.getPicture()%>" /><a href=""><%=i.getName()%></a></td>
					<td class="number"><%=i.getPrice() %></td>
					<td class="price" id="price_id_1">
						<span id = "price<%=i.getId() %>"><%=i.getPrice()*goods.get(i) %></span>
						<input type="hidden" value="" />
					</td>
					 <td>购买数量：<span id="sub" onclick="sub(<%=i.getId()%>);" style="padding:0 2px;border:1px #c0c0c0 solid;cursor:pointer;" >-</span>
						 <input type="text" id="number<%=i.getId() %>" name="number"  onblur="judge(<%=goods.get(i)%>);" value=<%= goods.get(i)%> size="2"/>
						 <span id="add" onclick="add(<%=i.getId()%>);" style="padding:0 2px;border:1px #c0c0c0 solid;cursor:pointer;">+</span>
					 </td>
                    <td class="delete">
					  <a href="cart?action=del&id=<%=i.getId()%>" onclick="delcfm();">删除</a>
					</td>
				</tr>
				     <% 
				         }
				     %>
				<!--循环的结束-->
				
			</table>
			 <div class="total"><span id="total">总计：<%=cart.calTotalPrice() %>￥</span></div>
			  <% 
			    }
			 %>
			<div class="button"><input type="submit" value="" /></div>
		</form>
	</div>
  </body>
</html>
