export type LcmWebResult<T> = {
    resultCode: number,
    resultMessage: string,
    resultBody: T
}

export type PagedList<T> = {
    totalCount: number,
    dataList: T[]
}

export type Menu = {
    menuTitle: string,
    menuUrl: string,
    children?: Menu[]
}
