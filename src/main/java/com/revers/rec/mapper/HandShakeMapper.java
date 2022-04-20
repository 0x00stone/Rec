package com.revers.rec.mapper;

import com.revers.rec.domain.HandShake;
import com.revers.rec.domain.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface HandShakeMapper {
    @Insert("insert into handShake(id,type,publicKey,aesKey) values(#{id},#{type},#{publicKey},#{aesKey});")
    public boolean insertHandShake(HandShake handShake);

    @Select("select * from handShake where id = #{id};")
    public HandShake findById(String id);

    @Select("select * from handShake;")
    public List<HandShake> findAll();

    @Delete("delete from handShake where id = #{id};")
    public void deleteById(String id);
}
