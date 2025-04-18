package is.rares.kumo.domain.content;


public enum ThumbnailSizeEnum {
    ORIGINAL(-1),
    FILE_EXPLORER_GRID(150), // real size 128
    FILE_EXPLORER_LIST(30) // real size 24
    ;


    ThumbnailSizeEnum(int maxSize) {
        this.maxSize = maxSize;
    }


    public int maxSize;


    public static ThumbnailSizeEnum getThumbnailSize(int width) {
        if (width == -1)
            return ThumbnailSizeEnum.ORIGINAL;


        var values = ThumbnailSizeEnum.values();
        for (int i = values.length - 1; i >= 1; i--) {
            System.out.println("Checking " + values[i].toString());
            if (values[i].maxSize >= width)
                return values[i];
        }
        
        return ThumbnailSizeEnum.ORIGINAL;
    }
}
