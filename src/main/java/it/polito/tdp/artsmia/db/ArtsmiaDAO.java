package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.EdgeModel;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Metodo 1 senza GROUP BY nel metodo del DAO -> Molto dispendioso perhcè fa 2 cicli for
	// SOLO SE IL DATABASE E' PICCOLO
//	public Integer getWeight(int sourceID, int targetID) {
//		
//		String sql = "SELECT e1.object_id AS o1, e2.object_id AS o2, COUNT(*) AS peso "
//				+ "FROM exhibition_objects e1, exhibition_objects e2 "
//				+ "WHERE e1.exhibition_id = e2.exhibition_id "
//				+ "AND e1.object_id = ? AND e2.object_id = ? ";
//		
//		
//		Connection conn = DBConnect.getConnection();
//
//		try {
//			PreparedStatement st = conn.prepareStatement(sql);
//			st.setInt(1, sourceID);
//			st.setInt(2, targetID);
//			ResultSet rs = st.executeQuery();
//			
//			rs.next();
//			
//			int peso = rs.getInt("peso");
//			
//			rs.close();
//			conn.close();
//			
//			
//			return peso;
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	
	// METODO 2 : con GROUP BY -> query più lenta nel DAO ma più veloce nel model
	public List<EdgeModel> getAllWeights(Map<Integer, ArtObject> objectIdMap) {
		
		String sql = "SELECT e1.object_id AS o1, e2.object_id AS o2, COUNT(*) AS peso "
				+ "FROM exhibition_objects e1, exhibition_objects e2 "
				+ "WHERE e1.exhibition_id = e2.exhibition_id "
				+ "AND e1.object_id > e2.object_id " // con il > evito di avere coppie speculari 
				+ "GROUP BY e1.object_id, e2.object_id "
				+ "ORDER BY peso DESC ";
		
		List<EdgeModel> allEdges = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				int sourceId = rs.getInt("o1");
				int targetId = rs.getInt("o2");
				
				EdgeModel e = new EdgeModel(objectIdMap.get(sourceId), 
								objectIdMap.get(targetId), 
								rs.getInt("peso"));
				allEdges.add(e);
			}
			
			rs.close();
			conn.close();
			
			
			return allEdges;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
