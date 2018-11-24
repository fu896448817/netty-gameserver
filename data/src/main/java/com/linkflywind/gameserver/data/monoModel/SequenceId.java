package com.linkflywind.gameserver.data.monoModel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 该 pojo 类主要为每个集合记录自增的序列
 *
 * @author Vijay
 *
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sequence")
public class SequenceId {
    @Id
    private String Id;
    @Field("collName")
    private String collName;
    @Field("seqId")
    private long seqId;
}