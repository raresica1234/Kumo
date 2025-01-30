package is.rares.kumo.domain.explore;


public enum ThumbnailSizeEnum {
    ORIGINAL(-1),
    MEDIUM(300),
    SMALL(150),
    EXTRA_SMALL(50);


    ThumbnailSizeEnum(int maxSize) {
        this.maxSize = maxSize;
    }


    public int maxSize;


    public static ThumbnailSizeEnum getThumbnailSize(int width) {
        if (width == -1)
            return ThumbnailSizeEnum.ORIGINAL;

        var values = ThumbnailSizeEnum.values();
        for (int i = values.length - 2; i >= 0; i--)
            if (values[i].maxSize >= width)
                return values[i];
        
        return ThumbnailSizeEnum.ORIGINAL;
    }
}
