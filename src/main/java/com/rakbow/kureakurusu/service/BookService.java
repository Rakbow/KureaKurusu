package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.BookMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.BookListParams;
import com.rakbow.kureakurusu.data.dto.CommonDetailQry;
import com.rakbow.kureakurusu.data.dto.SearchQry;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.vo.book.BookDetailVO;
import com.rakbow.kureakurusu.data.vo.book.BookMiniVO;
import com.rakbow.kureakurusu.data.vo.book.BookVOAlpha;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.BookVOMapper;
import com.rakbow.kureakurusu.util.entity.BookUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-28 23:45 book业务层
 */
@Service
@RequiredArgsConstructor
public class BookService extends ServiceImpl<BookMapper, Book> {

    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final BookMapper mapper;
    private final BookVOMapper VOMapper;
    private final PersonRelationMapper relationMapper;

    private final int ENTITY_VALUE = Entity.BOOK.getValue();

    @SneakyThrows
    @Transactional
    public BookDetailVO getDetail(CommonDetailQry qry) {
        Book book = getBook(qry.getId());
        if (book == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.BOOK.getName()));

        return BookDetailVO.builder()
                .item(VOMapper.toVO(book))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .itemImageInfo(CommonImageUtil.segmentImages(book.getImages(), 180, Entity.BOOK, false))
                .build();
    }

    @Transactional
    public Book getBook(long id) {
        if (AuthorityInterceptor.isSenior()) {
            return mapper.selectOne(new LambdaQueryWrapper<Book>().eq(Book::getId, id));
        }
        return mapper.selectOne(new LambdaQueryWrapper<Book>().eq(Book::getStatus, 1).eq(Book::getId, id));
    }

    @Transactional
    public void deleteBooks(List<Long> ids) {
        //get original data
        List<Book> items = mapper.selectBatchIds(ids);
        for (Book item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, item.getId());
        }
        //delete
        mapper.delete(new LambdaQueryWrapper<Book>().in(Book::getId, ids));
        //delete person relation
        relationMapper.delete(new LambdaQueryWrapper<PersonRelation>().eq(PersonRelation::getEntityType, ENTITY_VALUE).in(PersonRelation::getEntityId, ids));
    }

    @Transactional
    public SearchResult<BookMiniVO> searchBooks(SearchQry qry) {
        SimpleSearchParam param = new SimpleSearchParam(qry);
        if(param.keywordEmpty()) new SearchResult<>();

        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<Book>()
                .or().like(Book::getTitle, param.getKeyword())
                .or().like(Book::getTitleZh, param.getKeyword())
                .or().like(Book::getTitleEn, param.getKeyword())
                .orderByDesc(Book::getId);

        IPage<Book> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<BookMiniVO> items = VOMapper.toMiniVO(pages.getRecords());

        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public SearchResult<BookVOAlpha> getBooks(BookListParams param) {

        QueryWrapper<Book> wrapper = new QueryWrapper<Book>()
                .like("title", param.getTitle())
                .like("isbn10", param.getIsbn10())
                .like("isbn13", param.getIsbn13())
                .eq("region", param.getRegion())
                .eq("lang", param.getLang())
                .eq(param.getBookType() != -1, "bookType", param.getBookType())
                .eq(param.getHasBonus() != null, "hasBonus", param.getHasBonus())
                .orderBy(param.isSort(), param.asc(), param.sortField);

        IPage<Book> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<BookVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

     /**
      * isbn互相转换
      *
      * @param label,isbn 转换方式,isbn
      * @return isbn
      * @author rakbow
      */
     @SneakyThrows
     public String getISBN(String label, String isbn) {

         isbn = isbn.replaceAll("-", "");

         if(StringUtils.equals(label, "isbn13")) {
             if(isbn.length() != 10) {
                 throw new Exception(I18nHelper.getMessage("book.crud.isbn10.invalid"));
             }
             return BookUtil.getISBN13(isbn);
         }
         if(StringUtils.equals(label, "isbn10")) {
             if(isbn.length() != 13) {
                 throw new Exception(I18nHelper.getMessage("book.crud.isbn13.invalid"));
             }
             return BookUtil.getISBN10(isbn);
         }
         return null;
     }

}
