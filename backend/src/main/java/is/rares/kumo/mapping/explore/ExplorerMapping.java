package is.rares.kumo.mapping.explore;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.domain.explore.PathPoint;
import is.rares.kumo.domain.explore.Permission;
import is.rares.kumo.model.explore.ExplorerFileModel;
import is.rares.kumo.model.explore.FileTypeModel;
import jakarta.activation.MimetypesFileTypeMap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = GlobalMapperConfig.class, imports = FileTypeModel.class)
public interface ExplorerMapping {

    @Mapping(target = "name", source = "path")
    @Mapping(target = "fullPath", source = "path")
    @Mapping(target = "type", expression = "java(FileTypeModel.DIRECTORY)")
    @Mapping(target = "write", expression = "java(true)")
    @Mapping(target = "delete", expression = "java(true)")
    @Mapping(target = "modifyRoot", expression = "java(true)")
    ExplorerFileModel mapToModel(PathPoint pathPoint);

    @Mapping(target = "name", source = "pathPoint.path")
    @Mapping(target = "fullPath", source = "pathPoint.path")
    @Mapping(target = "type", expression = "java(FileTypeModel.DIRECTORY)")
    ExplorerFileModel mapToModel(Permission permission);


    @Mapping(target = "name", source = "name")
    @Mapping(target = "fullPath", source = "path")
    @Mapping(target = "type", expression = "java(getFileType(file))")
    @Mapping(target = "write", expression = "java(true)")
    @Mapping(target = "delete", expression = "java(true)")
    @Mapping(target = "modifyRoot", expression = "java(true)")
    ExplorerFileModel mapToModelForOwner(File file);


    // TODO: Refine this so getPermissionForFile is cached, no need to do so many damn filterings
    @Mapping(target = "name", source = "file.name")
    @Mapping(target = "fullPath", source = "file.path")
    @Mapping(target = "type", expression = "java(getFileType(file))")
    @Mapping(target = "write", expression = "java(getPermissionForFile(file, rootPermission, relevantPermissions).isWrite())")
    @Mapping(target = "delete", expression = "java(getPermissionForFile(file, rootPermission, relevantPermissions).isDelete())")
    @Mapping(target = "modifyRoot", expression = "java(getPermissionForFile(file, rootPermission, relevantPermissions).isModifyRoot())")
    ExplorerFileModel mapFileToModelForUser(File file, Permission rootPermission, List<Permission> relevantPermissions);

    default Permission getPermissionForFile(File file, Permission rootPermission, List<Permission> relevantPermissions) {
        var permissionOptional = relevantPermissions.stream()
                .filter(permission -> file.getPath().startsWith(permission.getPathPoint().getPath()))
                .max(Comparator.comparingInt(p -> p.getPathPoint().getPath().length()));

        return permissionOptional.orElse(rootPermission);
    }


    default FileTypeModel getFileType(File file) {
        if (file.isDirectory())
            return FileTypeModel.DIRECTORY;
        if (isImage(file))
            return FileTypeModel.IMAGE;

        return FileTypeModel.FILE;
    }

    default boolean isImage(File file) {
        String mimeType = new MimetypesFileTypeMap().getContentType(file);
        String type = mimeType.split("/")[0];
        return type.equals("image");
    }

    List<ExplorerFileModel> mapPathPointsToModel(List<PathPoint> pathPointList);
    List<ExplorerFileModel> mapPermissionsToModel(List<Permission> permissionList);

    List<ExplorerFileModel> mapFilesToModelForOwner(List<File> contents);

    default List<ExplorerFileModel> mapFilesToModelForUser(List<File> files, Permission rootPermission, List<Permission> relevantPermissions) {
        return files.stream().map(file -> mapFileToModelForUser(file, rootPermission, relevantPermissions)).collect(Collectors.toList());
    }
}
