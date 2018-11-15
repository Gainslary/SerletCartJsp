package Servlet;
/*
@author:zhengzhao
@time: 2018/08/20
*/
import dao.ItemsDAO;
import entity.Cart;
import entity.Items;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CartServlet extends javax.servlet.http.HttpServlet {
    private String action;
    private ItemsDAO itemsDAO = new ItemsDAO();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        if (request.getParameter("action") != null) {
            this.action = request.getParameter("action");
            if(action.equals("show")){
                request.getRequestDispatcher("/cart.jsp").forward(request,response);
            }

            if(action.equals("add")){
                if(addCart(request,response)){
                    request.getRequestDispatcher("/success.jsp").forward(request,response);
                }
                else {
                    request.getRequestDispatcher("/failure.jsp").forward(request,response);
                }
            }
            if(action.equals("del")){
                if(delCart(request,response)){
                    request.getRequestDispatcher("/cart.jsp").forward(request,response);
                }
                else {
                    request.getRequestDispatcher("/cart.jsp").forward(request,response);
                }
            }

            if(action.equals("sub")){
                JSONObject goodsInfo = subOrAdd(request,response , "sub");
                if(goodsInfo != null){
                     response.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
                     response.setCharacterEncoding("UTF-8");//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
                     PrintWriter out =response.getWriter() ;
                     out.write(goodsInfo.toString());
                     out.close();
                }
            }
            if(action.equals("addNum")){
                JSONObject goodsInfo = subOrAdd(request,response , "add");
                if(goodsInfo != null){
                     response.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
                     response.setCharacterEncoding("UTF-8");//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
                     PrintWriter out =response.getWriter() ;
                     out.write(goodsInfo.toString());
                     out.close();
                }
            }
        }

    }
    private boolean addCart(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)throws javax.servlet.ServletException, IOException{
        String id = request.getParameter("id");
        String num = request.getParameter("num");
        Items item = itemsDAO.getItemsById(Integer.parseInt(id));
        if(request.getSession().getAttribute("cart") == null){
            Cart cart = new Cart();
            request.getSession().setAttribute("cart",cart);
        }
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if(cart.addGoodsInCart(item,Integer.parseInt(num))){
            return true;
        }
        else {
            return false;
        }

    }
    private boolean delCart(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)throws javax.servlet.ServletException, IOException{
        String id = request.getParameter("id");
        String num = request.getParameter("num");
        if (id != null) {
            Items idItem = itemsDAO.getItemsById(Integer.parseInt(id));
            if (idItem != null) {
                if (request.getSession().getAttribute("cart") != null) {
                    Cart cart = (Cart) request.getSession().getAttribute("cart");
                    HashMap<Items,Integer> goods = cart.getGoods();
                    Set<Items> items = goods.keySet();
                    Iterator<Items> item = items.iterator();
                    while (item.hasNext()){
                        Items i = item.next();
                        if (idItem.equals(i)){//商品存在购物车中
                            if(cart.removeGoodsInCart(idItem)){
                                return true;//根据操作返回结果判断是是否删除成功
                            }
                            else {
                                return false;
                            }
                        }
                        else {
                            return false;//商品不在购物车中
                        }
                    }
                    return false;
                }
                else {
                    //购物车为空
                    return false;
                }
            }else {
                //id不存在
                return false;
            }

        }
        else {
            //参数不全
            return false;
        }
    }
    private JSONObject subOrAdd(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response ,String action)throws javax.servlet.ServletException, IOException{
        String id = request.getParameter("id");
        if (id != null) {//判断参数
            Items idItem = itemsDAO.getItemsById(Integer.parseInt(id));
            if (idItem != null) {//判断数据库中是否存在这个ID
                if (request.getSession().getAttribute("cart") != null) {//判断当前session中是否存在cart对象，也就是说判断当前购物车内是否有物品
                    Cart cart =(Cart) request.getSession().getAttribute("cart");
                    HashMap<Items,Integer> goods = cart.getGoods();
                    Set<Items> items = goods.keySet();
                    Iterator<Items> item = items.iterator();
                    while (item.hasNext()){
                        Items i = item.next();
                        if (idItem.equals(i)){
                            if (action.equals("add") ) {
                                goods.put(i,goods.get(i)+1);//更新购买数量
                            }
                            if (action.equals("sub")) {
                                goods.put(i,goods.get(i)-1);//更新购买数量
                            }
                            JSONObject jsonObject=new JSONObject() ;
                            jsonObject.put("num",goods.get(i));
                            jsonObject.put("allPrice",i.getPrice()*goods.get(i));
                            jsonObject.put("calTotalPrice",cart.calTotalPrice());
                            return jsonObject;
                        }
                    }
                    return null;
                }else {
                    return null;//session中没有cart对象，也就是说购物车内为空
                }
            }else {
                return null;//id不存在
            }
        }else {
            return null;//参数不全
        }
    }
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        doPost(request,response);
    }
}
