package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository (DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String INSERT_MESSAGE_SQL = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_MESSAGE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entry.getProjectId());
            ps.setLong(2, entry.getUserId());
            ps.setDate(3, Date.valueOf(entry.getDate()));
            ps.setInt(4, entry.getHours());
            return ps;
        }, keyHolder);

        long timeEntryID = keyHolder.getKey().longValue();
        TimeEntry timeEntry = find(timeEntryID);

        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String SELECT_WHERE_ID_MESSAGE_SQL ="Select * from time_entries where id = ?";

        try {
            TimeEntry timeEntry = jdbcTemplate.queryForObject(SELECT_WHERE_ID_MESSAGE_SQL, new Object[]{timeEntryId}, new TimeEntryMapper());
            return timeEntry;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        String SELECT_ALL_MESSAGE_SQL ="Select * from time_entries";
        List<TimeEntry> timeEntries = jdbcTemplate.query(SELECT_ALL_MESSAGE_SQL, new TimeEntryMapper());
        return timeEntries;
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry entry) {
        String UPDATE_MESSAGE_SQL = "Update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id ="+ timeEntryId;

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(UPDATE_MESSAGE_SQL);
            ps.setLong(1, entry.getProjectId());
            ps.setLong(2, entry.getUserId());
            ps.setDate(3, Date.valueOf(entry.getDate()));
            ps.setInt(4, entry.getHours());
            return ps;
        });

        return find(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {
        String DELETE_MESSAGE_SQL = "DELETE FROM time_entries where id = ?";
        Object[] args = new Object[] {timeEntryId};

        jdbcTemplate.update(DELETE_MESSAGE_SQL, args);

    }
}
