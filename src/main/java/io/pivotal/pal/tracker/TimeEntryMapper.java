package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TimeEntryMapper implements RowMapper<TimeEntry> {

    @Override
    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimeEntry timeEntry = new TimeEntry();

        timeEntry.setId(rs.getInt("id"));
        timeEntry.setProjectId(rs.getInt("project_id"));
        timeEntry.setUserId(rs.getInt("user_id"));
        Date date = rs.getDate("date");
        LocalDate localDate = date.toLocalDate();
        timeEntry.setDate(localDate);
        timeEntry.setHours(rs.getInt("hours"));

        return timeEntry;
    }
}
