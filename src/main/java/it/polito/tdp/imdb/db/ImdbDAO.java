package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void getVertici(Map<Integer, Director> idMap, Year anno) {
		String sql="SELECT * "
				+ "FROM directors d "
				+ "WHERE d.id IN ( "
				+ "SELECT DISTINCT md.director_id "
				+ "FROM movies_directors md, movies m "
				+ "WHERE md.movie_id=m.id AND m.year=?) ";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				if(!idMap.containsKey(director.getId())) {
					idMap.put(director.getId(), director);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Director> idMap, Year anno){
		String sql="SELECT md.director_id AS d1, md1.director_id AS d2,COUNT( DISTINCT r.actor_id) AS peso "
				+ "FROM movies_directors md, movies m, roles r, movies_directors md1, movies m1, roles r1 "
				+ "WHERE md.movie_id=m.id AND m.year=? AND md.movie_id=r.movie_id "
				+ "AND md1.movie_id=m1.id AND m1.year=? AND md1.movie_id=r1.movie_id "
				+ "AND md.director_id<>md1.director_id AND r.actor_id=r1.actor_id "
				+ "GROUP BY md.director_id, md1.director_id ";
		List<Adiacenza> result=new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			st.setInt(2, anno.getValue());
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("d1")) && idMap.containsKey(res.getInt("d2"))) {
					Director d1=idMap.get(res.getInt("d1"));
					Director d2=idMap.get(res.getInt("d2"));
					Integer peso=res.getInt("peso");
					result.add(new Adiacenza(d1, d2, peso));

				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	
	
}
