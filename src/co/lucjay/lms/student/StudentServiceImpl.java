package co.lucjay.lms.student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.lucjay.lms.DAO;

public class StudentServiceImpl extends DAO implements StudentService {
	private PreparedStatement psmt;
	private ResultSet rs;

	private final String ALLSELECT = "SELECT STUDENT_ID, STUDENT_NAME, STUDENT_DEPT, DEPT_NAME, STUDENT_BIRTHDAY FROM STUDENT, DEPT WHERE STUDENT_DEPT = DEPT_CODE ORDER BY STUDENT_ID";
	private final String SELECT = "SELECT STUDENT_ID, STUDENT_NAME, STUDENT_DEPT, DEPT_NAME, STUDENT_BIRTHDAY FROM STUDENT, DEPT WHERE STUDENT_DEPT = DEPT_CODE AND STUDENT_ID = ?";
	private final String INSERT = "INSERT INTO STUDENT (STUDENT_ID, STUDENT_NAME, STUDENT_DEPT, STUDENT_BIRTHDAY) VALUES (?,?,?,?)";

	@Override
	public List<StudentDto> allStudent() {
		List<StudentDto> list = new ArrayList<StudentDto>();
		StudentDto dto;
		try {
			psmt = conn.prepareStatement(ALLSELECT);
			rs = psmt.executeQuery();
			while (rs.next()) {
				dto = new StudentDto();
				dto.setStudent_id(rs.getString("student_id"));
				dto.setStudent_name(rs.getString("student_name"));
				dto.setStudent_dept(rs.getString("student_dept"));
				dto.setDept_name(rs.getString("dept_name"));
				dto.setStudent_birthday(rs.getDate("student_birthday"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public StudentDto select(StudentDto dto) {
		try {
			psmt = conn.prepareStatement(SELECT);
			psmt.setString(1, dto.getStudent_id());
			rs = psmt.executeQuery();
			dto = new StudentDto(); // 초기화된dto
			if (rs.next()) {
				dto.setStudent_id(rs.getString("student_id"));
				dto.setStudent_name(rs.getString("student_name"));
				dto.setStudent_dept(rs.getString("student_dept"));
				dto.setDept_name(rs.getString("dept_name"));
				dto.setStudent_birthday(rs.getDate("student_birthday"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public int insert(StudentDto dto) {
		int n = 0;
		try {
			psmt = conn.prepareStatement(INSERT);
			psmt.setString(1, dto.getStudent_id());
			psmt.setString(2, dto.getStudent_name());
			psmt.setString(3, dto.getStudent_dept());
			psmt.setDate(4, dto.getStudent_birthday());
			n = psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n;
	}

	@Override
	public int update(StudentDto dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(StudentDto dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isCheckId(String id) { // 학생의 학번을 비교한다. 'is' boolean값을 리턴시킬때만 붙인다.
		boolean b = true;
		String sql = "SELECT * FROM student WHERE student_id = ?";// sql구문
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				b = false; // 실행구문 빠져나온다.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b; // id가 존재하면 false , 존재하지않으면 true
	}

	public String loginCheck(String id, String pw) {
		String sql = "select student_name from student where student_id = (select id from login where id =? and password=?)";
		String result = null;
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			rs = psmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("student_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
