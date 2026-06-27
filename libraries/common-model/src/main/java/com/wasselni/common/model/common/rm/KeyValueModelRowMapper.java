package com.wasselni.common.model.common.rm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.wasselni.common.model.common.KeyValueModel;

@SuppressWarnings("unchecked")
public class KeyValueModelRowMapper<X, Y, Z>
    implements
      RowMapper<KeyValueModel<X, Y, Z>> {

  @Override
  public KeyValueModel<X, Y, Z> mapRow(ResultSet rs, int rowNum)
      throws SQLException {

    int i = 1;

    KeyValueModel<X, Y, Z> model = new KeyValueModel<X, Y, Z>();

    model.setKey((X) rs.getObject(i++));
    model.setValue((Y) rs.getObject(i++));
    model.setExtraValue((Z) rs.getObject(i++));

    return model;
  }
}
