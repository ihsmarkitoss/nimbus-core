package com.nimbusframework.nimbuscore.examples;

import com.nimbusframework.nimbuscore.annotations.document.DocumentStore;
import com.nimbusframework.nimbuscore.annotations.persistent.Attribute;
import com.nimbusframework.nimbuscore.annotations.persistent.Key;

@DocumentStore(tableName = "test")
public class DocumentStoreWithTableName {

  @Key
  private String string;

  @Attribute
  private Integer integer;

  public DocumentStoreWithTableName(String string, Integer integer) {
    this.string = string;
    this.integer = integer;
  }

  public String getString() {
    return string;
  }

  public Integer getInteger() {
    return integer;
  }

}
