package no.kristiania.dao;

import no.kristiania.database.MemberToTask;
import no.kristiania.database.ProjectTask;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MemberToTaskDao extends AbstractDao<MemberToTask> {

    public MemberToTaskDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(MemberToTask memberToTask) throws SQLException {
        insert(memberToTask, "INSERT INTO member_to_task (member_id, task_id) values (?, ?)");
    }

    public MemberToTask retrieve(Integer id) throws SQLException {
        return retrieve(id, "SELECT * FROM member_to_task WHERE id = ?");
    }

    public List<MemberToTask> list() throws SQLException {
        return list("SELECT * FROM member_to_task");
    }

    @Override
    protected void mapEntityToPreparedStatement(PreparedStatement statement, MemberToTask entity) throws SQLException {
        statement.setInt(1, entity.getId());
        statement.setInt(2, entity.getTaskId());
        statement.executeUpdate();
    }

    @Override
    protected MemberToTask mapRow(ResultSet rs) throws SQLException {
        MemberToTask memberToTask = new MemberToTask();
        memberToTask.setId(rs.getInt("id"));
        memberToTask.setId(rs.getInt("member_id"));
        memberToTask.setTaskId(rs.getInt("task_id"));
        return memberToTask;
    }
}
