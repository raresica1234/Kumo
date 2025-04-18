package is.rares.kumo.mapping.explore;

import java.io.File;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import is.rares.kumo.core.config.GlobalMapperConfig;
import is.rares.kumo.core.config.KumoConfig;
import is.rares.kumo.domain.explore.PathPoint;
import is.rares.kumo.domain.explore.Permission;
import is.rares.kumo.model.explore.ExplorerFileModel;
import is.rares.kumo.model.explore.FileTypeModel;
import jakarta.activation.MimetypesFileTypeMap;
import lombok.extern.slf4j.Slf4j;

@Mapper(config = GlobalMapperConfig.class, imports = {
    FileTypeModel.class,
    KumoConfig.class
})
@Slf4j
public abstract class ExplorerMapping {

    @Autowired
    KumoConfig kumoConfig;

    @Mapping(target = "name", source = "path", qualifiedByName = "normalizePath")
    @Mapping(target = "fullPath", source = "path", qualifiedByName = "normalizePath")
    @Mapping(target = "type", expression = "java(FileTypeModel.DIRECTORY)")
    @Mapping(target = "write", expression = "java(true)")
    @Mapping(target = "delete", expression = "java(true)")
    @Mapping(target = "modifyRoot", expression = "java(true)")
    public abstract ExplorerFileModel mapToModel(PathPoint pathPoint);

    @Mapping(target = "name", source = "pathPoint.path", qualifiedByName = "normalizePath")
    @Mapping(target = "fullPath", source = "pathPoint.path", qualifiedByName = "normalizePath")
    @Mapping(target = "type", expression = "java(FileTypeModel.DIRECTORY)")
    public abstract ExplorerFileModel mapToModel(Permission permission);


    @Mapping(target = "name", source = "name")
    @Mapping(target = "fullPath", source = "path", qualifiedByName = "normalizePath")
    @Mapping(target = "type", expression = "java(getFileType(file))")
    @Mapping(target = "write", expression = "java(true)")
    @Mapping(target = "delete", expression = "java(true)")
    @Mapping(target = "modifyRoot", expression = "java(true)")
    public abstract ExplorerFileModel mapToModelForOwner(File file);


    // TODO: Refine this so getPermissionForFile is cached, no need to do so many damn filterings
    @Mapping(target = "name", source = "file.name")
    @Mapping(target = "fullPath", source = "file.path", qualifiedByName = "normalizePath")
    @Mapping(target = "type", expression = "java(getFileType(file))")
    @Mapping(target = "write", expression = "java(getPermissionForFile(file, rootPermission, relevantPermissions).isWrite())")
    @Mapping(target = "delete", expression = "java(getPermissionForFile(file, rootPermission, relevantPermissions).isDelete())")
    @Mapping(target = "modifyRoot", expression = "java(getPermissionForFile(file, rootPermission, relevantPermissions).isModifyRoot())")
    public abstract ExplorerFileModel mapFileToModelForUser(File file, Permission rootPermission, List<Permission> relevantPermissions);

    protected Permission getPermissionForFile(File file, Permission rootPermission, List<Permission> relevantPermissions) {
        var permissionOptional = relevantPermissions.stream()
                .filter(permission -> file.getPath().startsWith(permission.getPathPoint().getPath()))
                .max(Comparator.comparingInt(p -> p.getPathPoint().getPath().length()));

        return permissionOptional.orElse(rootPermission);
    }


    protected FileTypeModel getFileType(File file) {
        if (file.isDirectory())
            return FileTypeModel.DIRECTORY;
        if (isImage(file))
            return FileTypeModel.IMAGE;

        return FileTypeModel.FILE;
    }

    protected boolean isImage(File file) {
        String mimeType = new MimetypesFileTypeMap().getContentType(file);
        String type = mimeType.split("/")[0];
        return type.equals("image");
    }

    public abstract List<ExplorerFileModel> mapPathPointsToModel(List<PathPoint> pathPointList);
    public abstract List<ExplorerFileModel> mapPermissionsToModel(List<Permission> permissionList);

    public abstract List<ExplorerFileModel> mapFilesToModelForOwner(List<File> contents);

    public List<ExplorerFileModel> mapFilesToModelForUser(List<File> files, Permission rootPermission, List<Permission> relevantPermissions) {
        return files.stream().map(file -> mapFileToModelForUser(file, rootPermission, relevantPermissions)).collect(Collectors.toList());
    }

    @Named("normalizePath")
    protected String normalizePath(String path) {
        if (path == null)
            return null;

		try {
			Path basePath = Paths.get(path).toRealPath(LinkOption.NOFOLLOW_LINKS).normalize();
			Path mediaPath = Paths.get(kumoConfig.getMediaPath()).toRealPath(LinkOption.NOFOLLOW_LINKS).normalize();

            if (basePath.startsWith(mediaPath)) {
                Path result = mediaPath.relativize(basePath);
                return result.toString();
            }
            return path;
		} catch (IOException e) {
            log.error("Normalizing path {} failed", path);
            return path;
		}
    }
}
