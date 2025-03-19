import React from 'react';
import Image from 'next/image';
import '@/styles/globals.css';

const Loading: React.FC = () => (
    <div className="loading-overlay">
        <Image
            src="/load-block.gif"
            alt="Loading..."
            width={100}
            height={100}
            className="loading-gif"
            unoptimized={true}
        />
    </div>
);

export default Loading;