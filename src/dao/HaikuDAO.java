package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.HaikuBean;
import haiku.Common;

public class HaikuDAO extends DAOConnection{
	//最新の歌を取得
	public ArrayList<HaikuBean> newHaiku(int genre){
		ArrayList<HaikuBean> newHaiku = new ArrayList<HaikuBean>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT * FROM haiku WHERE genre = ? ORDER BY compose_date DESC, compose_time DESC LIMIT 1");
			ps.setInt(1, genre);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				HaikuBean bean = new HaikuBean();
				bean.setId(rs.getInt("haiku_id"));
				bean.setHaiku(rs.getString("haiku"));
				bean.setComposer(rs.getString("composer"));
				bean.setComment(rs.getString("comment"));
				bean.setStrComposeDate(Common.composeDateChange(rs));
				bean.setStrComposeTime(Common.composeTimeChange(rs));
				bean.setGenre(rs.getInt("genre"));
				bean.setGood(rs.getInt("good"));
				newHaiku.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
		return newHaiku;
	}
	
	//高評価の歌を取得
	public ArrayList<HaikuBean> goodHaiku(int genre){
		ArrayList<HaikuBean> goodHaiku = new ArrayList<HaikuBean>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT * FROM haiku WHERE genre = ? ORDER BY good DESC LIMIT 3");
			ps.setInt(1, genre);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				HaikuBean bean = new HaikuBean();
				bean.setId(rs.getInt("haiku_id"));
				bean.setHaiku(rs.getString("haiku"));
				bean.setComposer(rs.getString("composer"));
				bean.setComment(rs.getString("comment"));
				bean.setStrComposeDate(Common.composeDateChange(rs));
				bean.setStrComposeTime(Common.composeTimeChange(rs));
				bean.setGenre(rs.getInt("genre"));
				bean.setGood(rs.getInt("good"));
				goodHaiku.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
		return goodHaiku;
	}
		
	//全句を取得
	public ArrayList<HaikuBean> selectList(int genre){
		ArrayList<HaikuBean> list = new ArrayList<HaikuBean>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT * FROM haiku WHERE genre = ? ORDER BY compose_date DESC, compose_time DESC");
			ps.setInt(1, genre);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				HaikuBean bean = new HaikuBean();
				bean.setId(rs.getInt("haiku_id"));
				bean.setHaiku(rs.getString("haiku"));
				bean.setComposer(rs.getString("composer"));
				bean.setComment(rs.getString("comment"));
				bean.setStrComposeDate(Common.composeDateChange(rs));
				bean.setStrComposeTime(Common.composeTimeChange(rs));
				bean.setGood(rs.getInt("good"));
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
		return list;
	}
	
	//俳号で検索
	public ArrayList<HaikuBean> searchList(String name, int genre){
		ArrayList<HaikuBean> searchList = new ArrayList<HaikuBean>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT * FROM haiku WHERE composer LIKE '%" + name + "%' AND genre = ? ORDER BY compose_date DESC, compose_time DESC");
			ps.setInt(1, genre);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				HaikuBean bean = new HaikuBean();
				bean.setId(rs.getInt("haiku_id"));
				bean.setHaiku(rs.getString("haiku"));
				bean.setComposer(rs.getString("composer"));
				bean.setComment(rs.getString("comment"));
				bean.setStrComposeDate(Common.composeDateChange(rs));
				bean.setStrComposeTime(Common.composeTimeChange(rs));
				bean.setGood(rs.getInt("good"));
				searchList.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
		return searchList;
	}
	
	//閲覧者が詠んだ歌を取得
	public ArrayList<HaikuBean> userHaiku(String name, int genre){
		ArrayList<HaikuBean> haikuList = new ArrayList<HaikuBean>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT * FROM haiku WHERE composer = ? AND genre = ? ORDER BY compose_date DESC, compose_time DESC");
			ps.setString(1, name);
			ps.setInt(2, genre);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				HaikuBean bean = new HaikuBean();
				bean.setId(rs.getInt("haiku_id"));
				bean.setHaiku(rs.getString("haiku"));
				bean.setComposer(rs.getString("composer"));
				bean.setComment(rs.getString("comment"));
				bean.setStrComposeDate(Common.composeDateChange(rs));
				bean.setStrComposeTime(Common.composeTimeChange(rs));
				bean.setGood(rs.getInt("good"));
				haikuList.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
		return haikuList;
	}
	
	//新たに句を詠む
	public void compose(String name, String haiku, String comment, int genre) {
		try {
			conn = getConnection();
			ps = conn.prepareStatement("INSERT INTO haiku (haiku, composer, comment, compose_date, compose_time, genre) VALUES (?, ?, ?, now(), now(), ?)");
			ps.setString(1, haiku);
			ps.setString(2, name);
			ps.setString(3, comment);
			ps.setInt(4, genre);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
	}
	
	//高評価を追加
	public void goodPlus(int id) {
		try {
			conn = getConnection();
			ps = conn.prepareStatement("UPDATE haiku SET good = good + 1 WHERE haiku_id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
	}
	
	//削除する歌を取得
	public ArrayList<HaikuBean> selectDeleteHaiku(int id){
		ArrayList<HaikuBean> deleteHaiku = new ArrayList<HaikuBean>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT * FROM haiku WHERE haiku_id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				HaikuBean bean = new HaikuBean();
				bean.setId(rs.getInt("haiku_id"));
				bean.setHaiku(rs.getString("haiku"));
				bean.setComposer(rs.getString("composer"));
				bean.setComment(rs.getString("comment"));
				bean.setStrComposeDate(Common.composeDateChange(rs));
				bean.setStrComposeTime(Common.composeTimeChange(rs));
				bean.setGood(rs.getInt("good"));
				deleteHaiku.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
		return deleteHaiku;
	}
	
	//指定された歌を削除
	public void deleteHaiku(int id) {
		try {
			conn = getConnection();
			ps = conn.prepareStatement("DELETE FROM haiku WHERE haiku_id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			allClose(ps, conn);
		}
	}
}