package lab4.backend.utils.mapping;

import lab4.backend.data.entities.ResultEntity;
import lab4.backend.dto.ResultDTO;


public class ResultMapper {
    public static ResultDTO entityToDTO(ResultEntity resultEntity) {
        ResultDTO resultDTO = new ResultDTO();

        resultDTO.setId(resultEntity.getId());
        resultDTO.setX(resultEntity.getX());
        resultDTO.setY(resultEntity.getY());
        resultDTO.setR(resultEntity.getR());
        resultDTO.setResult(resultEntity.getResult());
        resultDTO.setTime(resultEntity.getTime());
        resultDTO.setCurrentTime(resultEntity.getCurrentTime());
        resultDTO.setUser(UserMapper.entityToDTO(resultEntity.getUser()));

        return resultDTO;
    }

    public static ResultEntity dtoToEntity(ResultDTO resultDTO) {
        ResultEntity resultEntity = new ResultEntity();

        resultEntity.setX(resultDTO.getX());
        resultEntity.setY(resultDTO.getY());
        resultEntity.setR(resultDTO.getR());
        resultEntity.setResult(resultDTO.getResult());
        resultEntity.setTime(resultDTO.getTime());
        resultEntity.setCurrentTime(resultDTO.getCurrentTime());
        resultEntity.setUser(UserMapper.dtoToEntity(resultDTO.getUser()));

        return resultEntity;
    }
}
